//
//  AuthViewModel.swift
//  ecoquest-iOS
//
//  Created by Jonathan Arredondo on 9/24/24.
//

import SwiftUI
import GoogleSignIn
import GoogleSignInSwift

class AuthViewModel: ObservableObject {
    @Published var isSignedIn = false
    @Published var showError = false
    @Published var userId: String = ""
    @Published var idToken: String = ""
    @Published var isRefCodeModalPresented = false
    @Published var isLoading = false
    
    // Helper function to get the root view controller
    private func getRootViewController() -> UIViewController {
        guard let windowScene = UIApplication.shared.connectedScenes.first as? UIWindowScene,
              let rootVC = windowScene.windows.first?.rootViewController else {
            return UIViewController()
        }
        return rootVC
    }
    
    // Sign-in button logic
    func handleSignInButton() {
        isLoading = true // Activar estado de carga
        let rootViewController = getRootViewController()
        GIDSignIn.sharedInstance.signIn(withPresenting: rootViewController) { signInResult, error in
            DispatchQueue.main.async {
                 self.isLoading = false  // Desactivar estado de carga
             }
             
            guard error == nil else { return }
            guard let signInResult = signInResult else { return }
            
            signInResult.user.refreshTokensIfNeeded { user, error in
                guard error == nil else { return }
                guard let user = user else { return }
                self.idToken = user.idToken?.tokenString ?? ""
                self.userId = user.userID ?? ""
                
                // Show RefCodeInputModal
                DispatchQueue.main.async {
                    self.isRefCodeModalPresented = true
                }
                
                // Send ID token to backend
//                Task {
//                    await self.submitIdToken(idToken: idToken, userId: self.userId)
//                }
            }
        }
    }
    
    // Networking: submit the ID token to the backend
    func submitIdToken(invitedBy: String? = nil) async {
        var payload: [String: Any] = [
            "idToken": self.idToken,
            "userId": self.userId
        ]

        if let invitedBy = invitedBy {
            payload["invitedBy"] = invitedBy
        }

        guard let authData = try? JSONSerialization.data(withJSONObject: payload) else {
            return
        }
        
        let url = URL(string: "\(Config.serverURL)/auth/auth-user")!
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        
        do {
            let (data, response) = try await URLSession.shared.upload(for: request, from: authData)
            DispatchQueue.main.async {
                self.isLoading = false  // Desactivar estado de carga
            }
            if let httpResponse = response as? HTTPURLResponse {
                if (200...299).contains(httpResponse.statusCode), let responseData = String(data: data, encoding: .utf8) {
                    // Auth successful, redirect to main view
                    DispatchQueue.main.async {
                        self.isSignedIn = true
                    }
                } else {
                    // Show a modal with error code
                    DispatchQueue.main.async {
                        self.showError = true
                    }
                }
            }
        } catch {
            print("Error occurred: \(error.localizedDescription)")
        }
    }
}

