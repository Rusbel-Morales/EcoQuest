// getAllMissions.js
import connectWithConnector from '../../config/db/dbConnection.js';

/**
 * Retrieves all missions from the database.
 *
 * @returns {Promise<Array<{ id_mision: number, titulo: string, descripcion: string, categoria: string, dificultad: string }>>}
 *     A Promise resolving to an array of objects, each containing the mission details.
 */
const getAllMissions = async () => {
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

export default getAllMissions;
