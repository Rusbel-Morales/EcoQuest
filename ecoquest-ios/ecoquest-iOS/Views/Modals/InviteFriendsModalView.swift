import SwiftUI

struct InviteFriendsModalView: View {
    @Binding var isPresented: Bool
    @State private var showShareSheet = false
    @EnvironmentObject var authViewModel: AuthViewModel

    private var refCode: String {
        generateReferralCode(userId: authViewModel.userId)
    }
    
    private var shareMessage: String {
        "¡Únete a EcoQuest y gana puntos completando misiones! Código de invitación: \(refCode)"
    }
    
    var body: some View {
        ZStack(alignment: .topTrailing) {
            VStack(spacing: 20) {
                // Title
                Text("Invita a tus amigos a EcoQuest!")
                    .font(.title3)
                    .fontWeight(.bold)
                    .multilineTextAlignment(.center)
                    .foregroundColor(.primary)
                    .padding(.top, 20)
                
                // Subtitle
                Text("Comparte tu código con tus amigos para invitarlos a descargar la app. Al completar su primera misión, ambos ganarán EcoXP.")
                    .font(.body)
                    .foregroundColor(.secondary)
                    .multilineTextAlignment(.center)
                    .padding(.horizontal, 20)
                
                // Referral code
                Text(refCode)
                    .font(.title)
                    .fontWeight(.bold)
                    .foregroundColor(.primary)
                    .padding(.vertical, 8)
                    .frame(maxWidth: .infinity)
                    .background(Color.white.opacity(0.2))
                    .cornerRadius(8)
                
                // Share button
                Button(action: {
                    showShareSheet = true
                }) {
                    HStack {
                        Image(systemName: "square.and.arrow.up")
                            .resizable()
                            .frame(width: 20, height: 20)
                        Text("Compartir")
                            .font(.body)
                            .fontWeight(.semibold)
                    }
                    .padding()
                    .frame(maxWidth: .infinity)
                    .background(Color.white)
                    .foregroundColor(.primary)
                    .cornerRadius(10)
                    .shadow(color: Color.black.opacity(0.2), radius: 4, x: 0, y: 2)
                }
                .sheet(isPresented: $showShareSheet) {
                    ShareSheet(activityItems: [shareMessage])
                }
                
            }
            .padding()
            .frame(width: 320)
            .background(Color(red: 181/255, green: 222/255, blue: 135/255))
            .cornerRadius(20)
            .shadow(color: .black.opacity(0.2), radius: 8, x: 0, y: 4)
            
            // Close button
            Button(action: {
                isPresented = false
            }) {
                Image(systemName: "xmark.circle.fill")
                    .resizable()
                    .frame(width: 24, height: 24)
                    .foregroundColor(.gray)
                    .opacity(0.7)
            }
            .padding(10)
        }
    }
}

// Updated ShareSheet struct
struct ShareSheet: UIViewControllerRepresentable {
    var activityItems: [Any]
    var applicationActivities: [UIActivity]? = nil

    func makeUIViewController(context: Context) -> UIActivityViewController {
        UIActivityViewController(activityItems: activityItems, applicationActivities: applicationActivities)
    }

    func updateUIViewController(_ uiViewController: UIActivityViewController, context: Context) {}
}

#Preview {
    InviteFriendsModalView(isPresented: .constant(true))
}
