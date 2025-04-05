import verifyIdToken from '../middlewares/verifyIdToken.js';  // Assume this middleware verifies the token
import {getUserByEmail, createUserInDb, getUserByRefCode, addBonusPoints} from '../services/userService.js';
import {assignInitialOptionalMissions} from "../services/missionService.js";

export const verifyUser = async (req, res) => {
	const { idToken, userId, deviceToken, invitedBy = null } = req.body;

	if (!idToken) {
		return res.status(403).json({ error: 'No token provided' });
	}

	try {
		// Verificar ID Token para obtener información de usuario
		const { email, name } = await verifyIdToken(idToken);

		// Revisar que el usuario no exista en la base de datos
		const user = await getUserByEmail(email);

		// Crear usuario si no existe
		if (!user) {
			// Mapear ref_code a id de usuario invitador
			const { id_usuario: invitadorId} = await getUserByRefCode(invitedBy) || { id_usuario : null };
			await createUserInDb(userId, name, email, deviceToken, invitadorId)
			// Agregar bonus de puntos a ambos usuarios solo si hay invitador
			invitadorId && await addBonusPoints(invitadorId, userId, 100);
			// Asignarle al usuario sus tres misiones opcionales iniciales
			await assignInitialOptionalMissions(userId);
		}

		res.status(200).json({ message: 'Token verified successfully' });
	} catch (error) {
		console.error('Error verifying token:', error);
		res.status(500).json({ error: 'Error verifying token' });
	}
};