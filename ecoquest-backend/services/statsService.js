import connectWithConnector from "../config/db/dbConnection.js";

/**
 * Retrieves the current and longest streaks for a user from the database.
 *
 * @param {number} userId The ID of the user to retrieve the streaks for.
 * @returns {Promise<{ racha_actual: number, racha_maxima: number } | null>}
 *     A Promise resolving to an object containing the current and longest
 *     streaks for the user, or null if the user does not exist in the database.
 */
export const getCurrentAndLongestStreak = async (userId) => {
	const pool = await connectWithConnector();

	const query = `
		SELECT racha_actual, racha_maxima
		FROM user_stats
		WHERE id_usuario = ?
			`;

	const [rows] = await pool.query(query, [userId])

	// Clean up the pool connection
	await pool.end();

	if (rows.length === 0) {
		return null;
	}

	return rows[0];
}

export const queryUserStats = async (userId) => {
	const pool = await connectWithConnector();

	const query = `SELECT racha_actual, racha_maxima, total_puntos, total_misiones_completadas FROM user_stats WHERE id_usuario = ?`;

	const [rows] = await pool.query(query, [userId]);

	// Clean up the pool connection
	await pool.end();

	return rows[0]
}

export const queryLeaderboard = async () => {
	const pool = await connectWithConnector();

	const query = `SELECT * FROM weekly_leaderboard`;

	const [rows] = await pool.query(query);

	// Clean up the pool connection
	await pool.end();

	return rows
}

export const queryXpBarData = async (userId) => {
	const pool = await connectWithConnector();

	const query = `
SELECT
    cu.total_puntos AS mis_puntos,
    cu.rank_position AS mi_posicion,
    COALESCE(nu.total_puntos, NULL) AS puntos_siguiente_usuario
FROM (
    SELECT
        id_usuario,
        SUM(puntos) AS total_puntos,
        RANK() OVER (ORDER BY SUM(puntos) DESC) AS rank_position
    FROM usuario_mision
    WHERE status = 'completada'
    GROUP BY id_usuario
) AS cu
LEFT JOIN (
    SELECT
        id_usuario,
        SUM(puntos) AS total_puntos,
        RANK() OVER (ORDER BY SUM(puntos) DESC) AS rank_position
    FROM usuario_mision
    WHERE status = 'completada'
    GROUP BY id_usuario
) AS nu ON nu.rank_position = cu.rank_position - 1
WHERE cu.id_usuario = ?;
	`;

	const [rows] = await pool.query(query, [userId]);

	// Clean up the pool connection
	await pool.end();

	return rows[0]
}

export const queryAchievements = async (userId) => {
	const pool = await connectWithConnector();

	const query = `
		SELECT 
    a.id_achievement,
    CASE
        WHEN ua.id_usuario IS NOT NULL THEN TRUE 
        ELSE FALSE 
    END AS isCompleted
FROM achievement a
LEFT JOIN usuario_achievement ua 
    ON a.id_achievement = ua.id_achievement 
    AND ua.id_usuario = ?;
	`

	const [rows] = await pool.query(query, [userId]);

	// Clean up the pool connection
	await pool.end();

	return rows;
}

export const queryTopLeaderboard = async () => {
	const pool = await connectWithConnector();

	const query = `
	SELECT id_usuario FROM weekly_leaderboard
	ORDER BY total_puntos DESC
	LIMIT 3;
	`;

	const [rows] = await pool.query(query);

	// Clean up the pool connection
	await pool.end();

	return rows;
}

export const queryTrophyCount = async (userId) => {
	const pool = await connectWithConnector();

	const query = `SELECT total_oro, total_plata, total_bronce FROM count_trophies_user WHERE id_usuario = ?`;

	const [rows] = await pool.query(query, [userId]);

	// Clean up the pool connection
	await pool.end();

	// Cast each field to an integer
	return {
		total_oro: parseInt(rows[0].total_oro, 10),
		total_plata: parseInt(rows[0].total_plata, 10),
		total_bronce: parseInt(rows[0].total_bronce, 10)
	};
};