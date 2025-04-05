// getDailyMission.js
import sendMessage from '../../services/aiService.js';
import getUserStats from '../getUserStats/getUserStats.js';
import missionStorage from '../../data/MissionStorage.js';
import {getCompletedDailyMissions} from "../../services/missionService.js";

/**
 * Obtiene la misión diaria para un usuario dado en base a sus estadisticas de misiones completadas.
 * Si no se proporciona el id del usuario, se considera que se quiere la misión diaria para un usuario
 * sin datos.
 * Se hace una llamada a la IA para que seleccione una misión diaria en base a los datos del usuario.
 * Si no hay datos del usuario, se pide a la IA que seleccione una misión diaria que se considere adecuada
 * para iniciar.
 * La respuesta de la IA se interpreta como el id de una misión del array de misiones.
 * La función devuelve el id de la misión seleccionada.
 *
 * @param {number} [userId=0] El id del usuario para el cual se quiere obtener la misi n diaria.
 * @returns {number} El id de la misi n diaria seleccionada.
 * @throws {Error} Si hubo un error al llamar a la IA o si la respuesta no fue un id de misi n v lido.
 */
const assignNewDailyMission = async (userId = 0) => {
	try {
		// Obtener data del usuario
		const userData = await getUserStats(userId);

		const completedMissions = await getCompletedDailyMissions(userId);

		// Llamar a la IA
		const prompt = `Selecciona una misión diaria para el usuario con estadisticas de misiones completadas: ${JSON.stringify(
			userData
		)}. Considera los identificadores de las misiones previamente completadas, para evitar repetirse: ${JSON.stringify(completedMissions)}. En caso de no haber datos, selecciona una misión diaria (de dificultad facil) que se considere adecuada iniciar. Regresa UNICAMENTE el id de la misión. Estas son las misiones: ${JSON.stringify(
			missionStorage.missions
		)}`;

		const selectedMissionId = await sendMessage(prompt);

		// Obtener detalles de misión en base a id de la IA
		const selectedMission = missionStorage.missions.find(
			mission => mission.id_mision === selectedMissionId
		);
		// Return id of the selected mission
		return selectedMission.id_mision;
	} catch (error) {
		throw error;
	}
};

export default assignNewDailyMission;
