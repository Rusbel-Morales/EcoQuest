/**
 * Clase que representa un almacenamiento de misiones.
 * Permite gestionar una lista de misiones, incluyendo la posibilidad de establecer
 * un nuevo conjunto de misiones.
 */
class MissionStorage {
	/**
	 * Constructor de la clase MissionStorage.
	 * Inicializa el almacenamiento con un conjunto de misiones.
	 * @param {Array<Object>} missions - Arreglo de objetos de misión, donde cada objeto sigue la estructura:
	 * {
	 *   "mission": {
	 *     "id_mision": Number,
	 *     "titulo": String,
	 *     "descripcion": String,
	 *     "categoria": String,
	 *     "dificultad": String
	 *   }
	 * }
	 */
	constructor(missions) {
		/**
		 * @type {Array<Object>} Almacenamiento de las misiones.
		 */
		this.missions = missions;
	}

	/**
	 * Establece un nuevo conjunto de misiones en el almacenamiento.
	 * @param {Array<Object>} missions - Arreglo de misiones que reemplaza al conjunto actual.
	 */
	setMissions(missions) {
		this.missions = missions;
	}
}

// Exporta una instancia de MissionStorage con una lista de misiones inicial vacía.
const missionStorage = new MissionStorage([]);
export default missionStorage;