import SwiftUI

struct EstadisticasDetailView: View {
    @Binding var path: NavigationPath
    @EnvironmentObject var authViewModel: AuthViewModel
    
    var body: some View {
        VStack(spacing: 20) {
            // Título section with safe area consideration for the notch
            VStack {
                Text("Logros y Trofeos")
                    .font(.title)
                    .bold()
            }
            .padding()
            .frame(maxWidth: .infinity, maxHeight: 120, alignment: .bottom)
            .background(Color(red: 181/255, green: 222/255, blue: 135/255))
            .cornerRadius(10)
            
            // Content sections
            LogrosView()
            TrofeosView()
            Spacer()
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .edgesIgnoringSafeArea(.top)
        .background(Color(red: 245/255, green: 245/255, blue: 245/255))
    }
}

#Preview {
    EstadisticasDetailView(path: .constant(NavigationPath()))
}
