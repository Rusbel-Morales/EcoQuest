import connectWithConnector from '../../config/db/dbConnection.js';

/**
 * Retrieves a list of all users who have a device token (i.e., users who have
 * opted in to receive push notifications).
 *
 * @returns {Promise<Array<{ id_usuario: number, device_token: string }>>}
 *     A Promise resolving to an array of objects, each containing the user ID
 *     and their corresponding device token.
 */
const getAllUsers = async () => {
	const pool = await connectWithConnector();
	const query = 'SELECT id_usuario, device_token FROM usuario';

	const [rows] = await pool.execute(query);
	return rows;
};

export default getAllUsers;
