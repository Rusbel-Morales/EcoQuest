//
//  XpBarViewModel.swift
//  ecoquest-iOS
//
//  Created by Jonathan Arredondo on 10/17/24.
//
import SwiftUI

struct XPBarDataResponse: Codable {
    let miPosicion: Int
    let misPuntos: Int
    let puntosSiguienteUsuario: Int?
}

class XpBarViewModel: ObservableObject {
    @Published var xpBarData: XPBarDataResponse?
    @Published var errorMessage: String?
    
    func fetchXpBarData(userId: String) {
        guard let url = URL(string: "\(Config.serverURL)/stats/get-xp-bar-data?userId=\(userId)") else {
            return
        }
        
        var request = URLRequest(url: url)
        request.httpMethod = "GET"
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")
        
        URLSession.shared.dataTask(with: request) { [weak self] data, response, error in
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
                let xpBarDataResponse = try JSONDecoder().decode(XPBarDataResponse.self, from: data)
                DispatchQueue.main.async {
                    self?.xpBarData = xpBarDataResponse
                }
            } catch let error {
                DispatchQueue.main.async {
                    self?.errorMessage = error.localizedDescription
                }
            }
        }.resume()
    }
}
