// sendPushNotification.js
import admin from '../../config/firebaseConfig.js';

/**
 * Sends a push notification to a single device.
 *
 * @param {string} deviceToken The unique token for the device to send to.
 * @param {string} title The title of the notification.
 * @param {string} body The body of the notification.
 * @return {Promise} A promise that resolves if the notification is sent
 *     successfully, or rejects if there is an error.
 */
const sendPushNotification = (deviceToken, title, body) => {
	const message = {
		notification: {
			title: title,
			body: body,
		},
		token: deviceToken,
	};

	admin
		.messaging()
		.send(message)
		.then(response => {
			console.log('Successfully sent message:', response);
		})
		.catch(error => {
			console.log('Error sending message:', error);
		});
};

export default sendPushNotification;
