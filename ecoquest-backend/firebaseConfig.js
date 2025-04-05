// firebaseConfig.js
const admin = require('firebase-admin');
const path = require('path');
require('dotenv').config(); // Para cargar las variables de entorno desde un archivo .env

// Cargar el archivo JSON de credenciales de Firebase desde la carpeta config
const serviceAccount = require(path.join(__dirname, '"C:/Users/Nivek/Downloads/json/ecoquest-2f063-firebase-adminsdk-15mgd-77b4ad2ae9.json"'));

admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
});

module.exports = admin;
