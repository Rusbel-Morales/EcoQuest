// scheduleGetDailyMission.js

import cron from 'node-cron';
import assignNewDailyMission from '../assignNewDailyMission/assignNewDailyMission.js';
import getAllUsers from '../getAllUsers/getAllUsers.js';
import connectWithConnector from '../../config/db/dbConnection.js';
import sendPushNotification from '../sendPushNotification/sendPushNotification.js';

/**
 * Schedules a daily mission assignment cron job that runs at 9:00 AM every day.
 * When the job runs, it fetches all users and assigns a new daily mission to each
 * user using the assignNewDailyMission function. The assigned mission is then
 * inserted into the usuario_mision table. If an error occurs during mission
 * assignment, it is logged to the console. If an error occurs during the
 * assignment process, it is also logged to the console.
 */
const scheduleAssignDailyMission = async (timeExpression) => {
	cron.schedule(timeExpression, async () => {
		console.log('Running daily mission assignment at 9:00 AM...');

		try {
			// Fetch users when the cron job runs to ensure up-to-date data
			const users = await getAllUsers();

			// Iterate over all users and assign missions in parallel using Promise.all
			await Promise.all(
				users.map(async user => {
					try {
						// Assign a new daily mission to the user
						const selectedMissionId = await assignNewDailyMission(
							user.id_usuario
						);

						console.log(
							`Assigned mission ${selectedMissionId} to user ${user.id_usuario}.`
						);

						const pool = await connectWithConnector();

						// Insert the assigned mission into the usuario_mision table
						const query =
							'INSERT INTO usuario_mision (id_usuario, id_mision) VALUES (?, ?)';
						await pool.query(query, [user.id_usuario, selectedMissionId]);

						// Send a notification to the user about the new daily mission assignment
						if (user.device_token) {
							await sendPushNotification(
								user.device_token,
								'¡Has recibido una nueva misión diaria!',
								'¡Aprovecha esta nueva misión diaria!'
							);
						}
					} catch (error) {
						console.error(
							`Error assigning mission to user ${user.id_usuario}:`,
							error
						);
					}
				})
			);

			console.log('Daily mission assignment completed successfully.');
		} catch (error) {
			console.error('Error running daily mission assignment:', error);
		}
	});
};

export default scheduleAssignDailyMission;
