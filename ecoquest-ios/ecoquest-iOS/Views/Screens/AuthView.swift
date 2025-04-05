import SwiftUI
import GoogleSignIn
import GoogleSignInSwift

struct AuthView: View {
    @Binding var path: NavigationPath
    @ObservedObject var viewModel = AuthViewModel()
    
    var body: some View {
        ZStack(alignment: .bottom) {
            VStack(spacing: 20) {
                HStack {
                    LogoView(imageName: "logonegro", width: 80, height: 80, opacity: 0.2)
                    Spacer()
                }
                LogoView(imageName: "logo-ecoquest", width: 260, height: nil)
                
                Text("¡Ayúdanos a hacer un mundo mejor!")
                    .font(.headline)
                    .multilineTextAlignment(.center)
                    .padding(.horizontal)
                
                SignInCard {
                    viewModel.handleSignInButton()
                }
                
                Spacer()
            }
            .padding(.horizontal, 24)
            .padding(.top, 50)
            
            Image("arbol")
                .resizable()
                .aspectRatio(contentMode: .fit)
                .frame(width: 150)
                .padding(.bottom, 20)
        }
        .onChange(of: viewModel.isSignedIn) {
            if viewModel.isSignedIn {
                path.append(AppRoute.missions)
            }
        }
        .toast(isPresenting: $viewModel.showError) {
            GlobalErrorView.showError("Login fallido, vuelve a intentar", in: $viewModel.showError)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(Color(red: 245/255, green: 245/255, blue: 245/255))
        .ignoresSafeArea(edges: .bottom)
        .overlay(
            Group {
                if viewModel.isLoading {
                    ZStack {
                        Color.black.opacity(0.3)
                            .ignoresSafeArea()
                        ProgressView("Iniciando sesión...")
                            .progressViewStyle(CircularProgressViewStyle())
                            .padding()
                            .background(Color.white)
                            .cornerRadius(10)
                    }
                }
            }
        )
        .overlay(
            Group {
                if viewModel.isRefCodeModalPresented {
                    Color.black.opacity(0.5)
                        .ignoresSafeArea()
                    RefCodeInputModalView(isPresented: $viewModel.isRefCodeModalPresented)
                }
            }
        )
    }
}

struct LogoView: View {
    var imageName: String
    var width: CGFloat?
    var height: CGFloat?
    var opacity: Double = 1.0
    
    var body: some View {
        Image(imageName)
            .resizable()
            .aspectRatio(contentMode: .fit)
            .frame(width: width, height: height)
            .opacity(opacity)
    }
}

struct SignInCard: View {
    var onSignInTapped: () -> Void
    
    var body: some View {
        ZStack {
            RoundedRectangle(cornerRadius: 12.0)
                .fill(Color(red: 0.745, green: 0.863, blue: 0.455))
                .frame(width: 320, height: 220)
                .shadow(radius: 4)
            
            VStack(spacing: 20) {
                Text("Registrarse con Google")
                    .font(.system(size: 24, weight: .bold))
                    .foregroundColor(.primary)
                GoogleSignInButton(action: onSignInTapped)
                    .frame(width: 240, height: 50)
                    .cornerRadius(8)
            }
            .padding()
        }
    }
}

#Preview {
    AuthView(path: .constant(NavigationPath()))
}
