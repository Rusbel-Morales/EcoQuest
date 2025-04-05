//
//  TrofeosViewModel.swift
//  ecoquest-iOS
//
//  Created by Jonathan Arredondo on 11/8/24.
//

import Foundation

struct TrophyCountResponse: Codable {
    let trophyCount: TrophyCount
}

struct TrophyCount: Codable {
    let totalOro: Int
    let totalPlata: Int
    let totalBronce: Int

    enum CodingKeys: String, CodingKey {
        case totalOro = "total_oro"
        case totalPlata = "total_plata"
        case totalBronce = "total_bronce"
    }
}

class TrofeosViewModel: ObservableObject {
    @Published var trophyCount: TrophyCount?
    @Published var errorMessage: String?
    @Published var isLoading: Bool = false
    
    func fetchTrophyCount(userId: String) {
        guard let url = URL(string: "\(Config.serverURL)/stats/get-user-trophies?userId=\(userId)") else {
            return
        }
        
        isLoading = true // Activar estado de carga
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
                let trophyCountResponse = try JSONDecoder().decode(TrophyCountResponse.self, from: data)
                DispatchQueue.main.async {
                    self?.trophyCount = trophyCountResponse.trophyCount
                }
            } catch let error {
                DispatchQueue.main.async {
                    self?.errorMessage = error.localizedDescription
                }
            }
        }.resume()
    }
}
