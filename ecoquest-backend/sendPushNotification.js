// sendPushNotification.js
const admin = require('./firebaseConfig');

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

module.exports = sendPushNotification;
