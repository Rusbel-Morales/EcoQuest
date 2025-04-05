import admin from 'firebase-admin';
import { fileURLToPath } from 'url';
import { dirname, join } from 'path';
import dotenv from 'dotenv';

// Obtener la ruta del archivo actual
const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);

// Cargar el archivo .env
dotenv.config();

// Cargar el archivo JSON de credenciales de Firebase desde la carpeta config
const serviceAccount = join(process.env.FIREBASE_ADMIN_SDK_PATH);

admin.initializeApp({
	credential: admin.credential.cert(serviceAccount),
});

export default admin;
