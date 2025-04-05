import Foundation
import Combine

struct Mission: Codable, Identifiable {
    let id_mision: Int
    let titulo: String
    let descripcion: String
    let categoria: String
    let dificultad: String
    
    // Conform to Identifiable by providing an `id` computed property
    var id: Int {
        return id_mision
    }
}

struct MissionResponse: Codable {
    let mission: Mission
    let isMissionCompleted: Bool

    // Custom decoding to convert "isMissionCompleted" from Int to Bool
    enum CodingKeys: String, CodingKey {
        case mission
        case isMissionCompleted = "isMissionCompleted"
    }

    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        mission = try container.decode(Mission.self, forKey: .mission)
        
        // Convert Int (0 or 1) to Bool for "isMissionCompleted"
        let isCompletedInt = try container.decode(Int.self, forKey: .isMissionCompleted)
        isMissionCompleted = isCompletedInt == 1
    }
}

class MisionesViewModel: ObservableObject {
    @Published var dailyMission: Mission? = nil
    @Published var isMissionCompleted: Bool = false
    @Published var errorMessage: String?
    @Published var isFetching: Bool = false
    
    func fetchDailyMission(userId: String) {
        guard let url = URL(string: "\(Config.serverURL)/missions/get-daily-mission?userId=\(userId)") else {
            return
        }
        
        isFetching = true // Activar estado de carga
        var request = URLRequest(url: url)
        request.httpMethod = "GET"
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")
        
        URLSession.shared.dataTask(with: request) { [weak self] data, response, error in
            DispatchQueue.main.async {
                self?.isFetching = false
            }
            
            if let error = error {
                DispatchQueue.main.async {
                    self?.errorMessage = error.localizedDescription
                }
                return
            }
            
            guard let data = data else {
                DispatchQueue.main.async {
                    self?.errorMessage = "No data received"
                }
                return
            }
            
            do {
                let missionResponse = try JSONDecoder().decode(MissionResponse.self, from: data)
                DispatchQueue.main.async {
                    self?.dailyMission = missionResponse.mission
                    self?.isMissionCompleted = missionResponse.isMissionCompleted
                }
            } catch let error {
                DispatchQueue.main.async {
                    self?.errorMessage = error.localizedDescription
                }
            }
        }.resume()
    }
    
    // Complete mission
    func markMissionAsComplete(missionId: Int, userId: String) {
        guard let url = URL(string: "\(Config.serverURL)/missions/complete-mission") else {
            return
        }
        
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")
        
        // Set the request body
        let body: [String: Any] = ["userId": userId, "idMission": missionId]
        do {
            request.httpBody = try JSONSerialization.data(withJSONObject: body, options: [])
        } catch {
            DispatchQueue.main.async {
                self.errorMessage = error.localizedDescription
            }
            return
        }
        
        URLSession.shared.dataTask(with: request) { [weak self] data, response, error in
            if let error = error {
                DispatchQueue.main.async {
                    self?.errorMessage = error.localizedDescription
                }
                return
            }
            
            guard let httpResponse = response as? HTTPURLResponse, (200...299).contains(httpResponse.statusCode) else {
                DispatchQueue.main.async {
                    self?.errorMessage = "Failed to mark mission as complete."
                }
                return
            }
        }.resume()
    }
}
