// getUserStats.js
import connectWithConnector from '../../config/db/dbConnection.js';

const getUserStats = async userID => {
	const pool = await connectWithConnector();
	const query =
		'SELECT categoria, completed_missions_count FROM completed_missions_per_category WHERE id_usuario = ?';

	try {
		const [rows] = await pool.query(query, [userID]);
		return [rows];
	} catch (err) {
		throw new Error(err);
	}
};

export default getUserStats;
