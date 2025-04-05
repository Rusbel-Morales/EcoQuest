//
//  OptionalMissionsViewModel.swift
//  ecoquest-iOS
//
//  Created by Jonathan Arredondo on 10/28/24.
//

import Foundation

struct NewMissionResponse: Codable {
    let mission: Mission
}

class OptionalMissionsViewModel: ObservableObject {
    @Published var missions: [Mission?] = []
    @Published var errorMessage: String?
    @Published var isLoading: Bool = false
    @Published var isRerolling: [Int: Bool] = [:] // Estado de carga por misión
    @Published var isCompleting: [Int: Bool] = [:] // Tracks loading state for completing missions
    @Published var shouldNavToShareDescriptionView: Bool = false
    
    func fetchOptMissions(userId: String) {
        guard let url = URL(string: "\(Config.serverURL)/missions/get-opt-missions?userId=\(userId)&nMissions=3") else {
            return
        }
        
        isLoading = true // Comienza el estado de carga
        var request = URLRequest(url: url)
        request.httpMethod = "GET"
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")
        
        URLSession.shared.dataTask(with: request) { [weak self] data, response, error in
            DispatchQueue.main.async {
                self?.isLoading = false // Detener el estado de carga
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
                let missionsResponse = try JSONDecoder().decode([Mission].self, from: data)
                DispatchQueue.main.async {
                    self?.missions = missionsResponse
                }
            } catch let error {
                DispatchQueue.main.async {
                    self?.errorMessage = error.localizedDescription
                }
            }
        }.resume()
    }
    
    func postRerollOptMissions(userId: String, prevMissionId: Int, prevMissionIndex: Int) {
        guard let url = URL(string: "\(Config.serverURL)/missions/reroll-opt-mission") else {
            return
        }
        
        // Iniciar el estado de carga para la misión específica
        DispatchQueue.main.async {
            self.isRerolling[prevMissionIndex] = true
        }
        
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")
        
        // Create the request body with userId and missionId
        let requestBody: [String: Any] = [
            "userId": userId,
            "idMission": prevMissionId
        ]
        
        do {
            request.httpBody = try JSONSerialization.data(withJSONObject: requestBody, options: [])
        } catch {
            print("Failed to serialize JSON body: \(error)")
            return
        }
        
        URLSession.shared.dataTask(with: request) { [weak self] data, response, error in
            DispatchQueue.main.async {
                self?.isRerolling[prevMissionIndex] = false // Detener el estado de carga
            }
            
            if let error = error {
                DispatchQueue.main.async {
                    self?.errorMessage = error.localizedDescription
                }
                return
            }
            
            // If data is empty, set mission to nil at this index
            guard let data = data, !data.isEmpty else {
                DispatchQueue.main.async { self?.missions[prevMissionIndex] = nil }
                return
            }
            
            do {
                let newMissionResponse = try JSONDecoder().decode(NewMissionResponse.self, from: data)
                DispatchQueue.main.async {
                    self?.missions[prevMissionIndex] = newMissionResponse.mission
                }
            } catch let error {
                DispatchQueue.main.async {
                    self?.errorMessage = error.localizedDescription
                }
            }
        }.resume()
    }
    
    func postCompleteMission(userId: String, prevMissionId: Int, prevMissionIndex: Int) {
        guard let url = URL(string: "\(Config.serverURL)/missions/complete-opt-mission") else {
            return
        }
        
        DispatchQueue.main.async {
            self.isCompleting[prevMissionIndex] = true // Start loading for this mission
        }
        
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")
        
        // Create the request body with userId and missionId
        let requestBody: [String: Any] = [
            "userId": userId,
            "idMission": prevMissionId
        ]
        
        do {
            request.httpBody = try JSONSerialization.data(withJSONObject: requestBody, options: [])
        } catch {
            print("Failed to serialize JSON body: \(error)")
            return
        }
        
        URLSession.shared.dataTask(with: request) { [weak self] data, response, error in
            DispatchQueue.main.async {
                self?.isCompleting[prevMissionIndex] = false // Stop loading after response
            }
            
            if let error = error {
                DispatchQueue.main.async {
                    self?.errorMessage = error.localizedDescription
                    self?.isCompleting[prevMissionIndex] = false // Stop loading in case of error
                }
                return
            }
            
            // If data is empty, set mission to nil at this index
            guard let data = data, !data.isEmpty else {
                DispatchQueue.main.async { self?.missions[prevMissionIndex] = nil }
                return
            }
            
            do {
                let newMissionResponse = try JSONDecoder().decode(NewMissionResponse.self, from: data)
                DispatchQueue.main.async {
                    self?.missions[prevMissionIndex] = newMissionResponse.mission
                }
            } catch let error {
                DispatchQueue.main.async {
                    self?.errorMessage = error.localizedDescription
                }
            }
        }.resume()
    }
}
