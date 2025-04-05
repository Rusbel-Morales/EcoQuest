import connectWithConnector from "../config/db/dbConnection.js";

/**
 * Retrieves all missions from the database.
 *
 * @returns {Promise<Array<{ id_mision: number, titulo: string, descripcion: string, categoria: string, dificultad: string }>>}
 *     A Promise resolving to an array of objects, each containing the mission details.
 */
export const getAllMissions = async () => {
	try {
		// Connect to the database
		const pool = await connectWithConnector();

		const query = 'SELECT * FROM mision';

		console.log('Fetching all missions...');

		// Execute the query
		const [rows] = await pool.query(query);
		return rows;
	} catch (error) {
		throw error;
	}
};

/**
 * Retrieves the assigned mission for the given user in the given time range.
 *
 * @param {number} userId The ID of the user to retrieve the assigned mission for.
 * @param {string} startTimestamp The start of the time range in which to
 *     search for the assigned mission.
 * @param {string} endTimestamp The end of the time range in which to
 *     search for the assigned mission.
 * @returns {Promise<{ id_mision: number, is_completed: boolean } | null>}
 *     A Promise resolving to an object with the mission ID and its completion
 *     status, or null if no mission was found.
 */
export const getAssignedMissionForUser = async (userId, startTimestamp, endTimestamp) => {
	const pool = await connectWithConnector();
	const query = `
      SELECT DISTINCT um.id_mision,
                      um.status,
                      CASE WHEN um.status = 'completada' THEN TRUE ELSE FALSE END AS is_completed
      FROM usuario_mision um
               JOIN mision m ON um.id_mision = m.id_mision
      WHERE um.id_usuario = ?
        AND um.timestamp_asignada BETWEEN ? AND ?
        AND m.dificultad = 'facil';
`;
	const [rows] = await pool.query(query, [userId, startTimestamp, endTimestamp]);

	// Clean up the pool connection
	await pool.end();

	return rows.length > 0 ? rows[0] : null;
};

/**
 * Marks the mission with the given ID as completed for the given user.
 *
 * @param {number} userId The ID of the user to mark the mission as completed for.
 * @param {number} idMission The ID of the mission to mark as completed.
 * @param points
 * @returns {Promise<boolean>} A Promise resolving to true if the mission was marked as completed, or false if the mission was not found or was already completed.
 */
export const markMissionAsComplete = async (userId, idMission, points) => {
	const pool = await connectWithConnector();

	const query = `
    UPDATE usuario_mision SET status = 'completada', timestamp_completada = NOW(), puntos = ?
    WHERE id_mision = ? AND id_usuario = ? AND status <> 'completada'
  `;
	const [result] = await pool.query(query, [points, idMission, userId]);
	return result.affectedRows > 0;
};

export const getAssignedOptionalMissions = async (userId, nMissions) => {
	const pool = await connectWithConnector();

	const query = `
    SELECT DISTINCT m.id_mision, m.titulo, m.descripcion, m.categoria, m.dificultad
    FROM mision m
    JOIN usuario_mision um ON m.id_mision = um.id_mision
    WHERE um.id_usuario = ?
      AND um.status = 'asignada'
    	AND m.dificultad = 'dificil'
    ORDER BY RAND()
    LIMIT 3;
`;

	const [rows] = await pool.query(query, [userId, nMissions]);
	console.log(rows)

	// Clean up the pool connection
	await pool.end();

	return rows;
}

export const selectOptionalMissions = async (nMissions, userId) => {
	const pool = await connectWithConnector();

	const query = `
      SELECT m.id_mision, m.titulo, m.descripcion, m.categoria, m.dificultad
      FROM mision m
               LEFT JOIN usuario_mision um
                         ON m.id_mision = um.id_mision
                             AND um.id_usuario = ?
      WHERE m.dificultad = 'dificil'
        AND (um.id_mision IS NULL)
      ORDER BY RAND()
          LIMIT ?;`;

	const [rows] = await pool.query(query, [userId, nMissions]);

	// Clean up the pool connection
	await pool.end();

	return rows.map(mission => mission.id_mision);
}

export const assignInitialOptionalMissions = async (userId) => {
	const pool = await connectWithConnector();

	const assignedMissionIds = await selectOptionalMissions(3, userId);

	// Insertar las misiones asignadas en la tabla de usuario_mision
	for (const missionId of assignedMissionIds) {
		const query = `
			INSERT INTO usuario_mision (id_usuario, id_mision)
			VALUES (?, ?)
		`;

		await pool.query(query, [userId, missionId]);
	}

	// Clean up the pool connection
	await pool.end();
}

export const assignNewOptionalMission = async (userId) => {
	const pool = await connectWithConnector();

	const [assignedMissionId] = await selectOptionalMissions(1, userId);

	if (!assignedMissionId) {
		return; // No hay misiones disponibles
	}

	const query = `
	INSERT INTO usuario_mision (id_usuario, id_mision)
	VALUES (?, ?)
	`;

	await pool.query(query, [userId, assignedMissionId]);

	// Clean up the pool connection
	await pool.end();

	return assignedMissionId;
}

export const markMissionAsRerolled = async (userId, idMission) => {
	const pool = await connectWithConnector();

	const query = `UPDATE usuario_mision SET status = 'cancelada' WHERE id_usuario = ? AND id_mision = ?`;

	const [result] = await pool.query(query, [userId, idMission]);

	if (result.affectedRows === 0) {
		throw new Error('No se pudo cancelar la misión');
	}

	// Clean up the pool connection
	await pool.end();

	return result.affectedRows > 0;
}

/**
 * Selects graph data for the user based on the specified date range.
 *
 * @param {number} userId The ID of the user for whom to fetch graph data.
 * @returns {Promise<Array<{ dia: string, puntos_totales: number }>>}
 *     A Promise resolving to an array of objects containing the day and total points for that day.
 */
export const selectGraphData = async (userId) => {
	const pool = await connectWithConnector();
	const query = `
        SELECT dia, puntos_totales
        FROM puntos_por_dia
        WHERE id_usuario = ?
    `;
	const [rows] = await pool.query(query, [userId]);
	return rows;
};

export const retrieveAndClearPendingPoints = async (userId) => {
	const pool = await connectWithConnector();
	const connection = await pool.getConnection();
	await connection.beginTransaction();

	try {
		// Obtener los puntos pendientes y reiniciarlos en una sola transacción atomica
		const [rows] = await connection.query(
			'SELECT puntos_pendientes FROM usuario WHERE id_usuario = ? FOR UPDATE',
			[userId]
		);
		const pendingPoints = rows[0]?.puntos_pendientes || 0;

		// Reiniciar los puntos pendientes si hay puntos pendientes
		if (pendingPoints > 0) {
			await connection.query(
				'UPDATE usuario SET puntos_pendientes = 0 WHERE id_usuario = ? AND puntos_pendientes >= 0',
				[userId]
			);
		}

		// Confirmar la transacción
		await connection.commit();

		// Liberar la conexion de vuelta al pool
		connection.release();

		return pendingPoints;
	} catch (error) {
		// Rollback de la transaccion en caso de error
		await pool.rollback();
		connection.release();
		throw error;
	}
}

export const getCompletedDailyMissions = async (userId) => {
	try {
		const pool = await connectWithConnector();

		const query =
			`
			SELECT GROUP_CONCAT(M.id_mision) AS completed_missions
			FROM usuario_mision UM
			JOIN mision M ON UM.id_mision = M.id_mision
			WHERE UM.id_usuario = '106199581597724590099'
			  AND UM.status = 'completada'
			  AND M.dificultad = 'facil';
			`

		const [rows] = await pool.query(query, [userId]);

		// Clean up the pool connection
		await pool.end();

		return rows;
	} catch (err) {
		throw new Error(err);
	}
}



