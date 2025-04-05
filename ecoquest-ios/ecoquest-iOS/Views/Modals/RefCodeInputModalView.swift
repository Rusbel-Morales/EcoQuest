import SwiftUI

struct RefCodeInputModalView: View {
    @Binding var isPresented: Bool
    @State private var inputCode: String = ""
    @EnvironmentObject var authViewModel: AuthViewModel
    
    private var isCodeValid: Bool {
        inputCode.isEmpty || (inputCode.count == 5 && inputCode.allSatisfy { $0.isLetter || $0.isNumber })
    }
    
    var body: some View {
        VStack(spacing: 20) {
            // Header Text
            VStack(spacing: 10) {
                Text("¡Bienvenido a EcoQuest!")
                    .font(.title2)
                    .fontWeight(.bold)
                    .foregroundColor(.black)
                
                Text("Si alguien te lo recomendó, puedes ingresar su código aquí")
                    .font(.body)
                    .foregroundColor(.black)
                    .multilineTextAlignment(.center)
                    .padding(.horizontal)
            }
            
            // Input Field
            TextField("Ingresa el código", text: $inputCode)
                .padding()
                .background(Color.white.opacity(0.8))
                .cornerRadius(10)
                .frame(maxWidth: .infinity)
                .autocapitalization(.allCharacters)
                .disableAutocorrection(true)
                .padding(.horizontal, 20)
            
            // Buttons
            VStack(spacing: 12) {
                Button(action: {
                    isPresented = false
                    authViewModel.isLoading = true
                    Task {
                        await authViewModel.submitIdToken(invitedBy: inputCode)
                    }
                }) {
                    Text("Enviar")
                        .font(.body)
                        .fontWeight(.semibold)
                        .foregroundColor(.black)
                        .padding()
                        .frame(maxWidth: .infinity)
                        .background(isCodeValid ? Color.white : Color.gray.opacity(0.6))
                        .cornerRadius(10)
                }
                .disabled(!isCodeValid)
                
                Button(action: {
                    isPresented = false
                    authViewModel.isLoading = true
                    Task {
                        await authViewModel.submitIdToken()
                    }
                }) {
                    Text("No tengo código")
                        .font(.body)
                        .fontWeight(.semibold)
                        .foregroundColor(.black)
                        .padding()
                        .frame(maxWidth: .infinity)
                        .background(Color.orange)
                        .cornerRadius(10)
                }
            }
            .padding(.horizontal, 20)
        }
        .padding()
        .frame(width: 320)
        .background(Color.green)
        .cornerRadius(20)
        .shadow(color: .black.opacity(0.2), radius: 10, x: 0, y: 5)
    }
}

#Preview {
    RefCodeInputModalView(isPresented: .constant(true))
}
