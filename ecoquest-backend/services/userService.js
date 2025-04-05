import connectWithConnector from "../config/db/dbConnection.js";
import generateUserCode from "../utils/generateUserCode.js";

export const getUserByEmail = async (email) => {
	const pool = await connectWithConnector();
	const query = 'SELECT id_usuario FROM usuario WHERE email = ?';
	const [results] = await pool.query(query, [email]);

	if (results.length > 0) {
		return results[0];  // Return the user data if found
	} else {
		return null;  // Return null if the user doesn't exist
	}
};

export const createUserInDb = async (userId, name, email, deviceToken, invitedBy = null) => {
	const pool = await connectWithConnector();
	// Generar codigo para invitar referidos
	const refCode = generateUserCode(userId);
	const query = `
    INSERT INTO usuario (id_usuario, nombre, email, device_token, ref_code, id_invitador, timestamp, impacto_acumulado)
    VALUES (?, ?, ?, ?, ?, ?, NOW(), '')
  `;
	await pool.query(query, [userId, name, email, deviceToken, refCode, invitedBy]);
};

export const getUserByRefCode = async (refCode) => {
	const pool = await connectWithConnector();
	const query = 'SELECT id_usuario FROM usuario WHERE ref_code = ?';
	const [results] = await pool.query(query, [refCode]);

	if (results.length > 0) {
		return results[0];  // Return the user data if found
	} else {
		return null;  // Return null if the user doesn't exist
	}
}

export const addBonusPoints = async (referringUserId, referredUserId, points) => {
	const pool = await connectWithConnector();
	const query = 'UPDATE usuario SET puntos_pendientes = puntos_pendientes + ? WHERE id_usuario IN (?, ?)';
	const [rows] = await pool.query(query, [points, referredUserId, referringUserId]);
	if (rows.affectedRows === 0) {
		throw new Error('No se pudo agregar los puntos');
	}
}