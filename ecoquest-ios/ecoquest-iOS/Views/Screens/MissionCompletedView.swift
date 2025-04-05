import SwiftUI

struct MissionCompleteView: View {
    @Binding var path: NavigationPath
    @Binding var isPresented: Bool
    @Binding var isShareSheetPresented: Bool
    @Binding var completedMission: Mission?
    
    var body: some View {
        ZStack {
            if isPresented {
                VStack(spacing: 20) {
                    Spacer()
                    
                    // Success Image
                    Image(systemName: "checkmark.seal.fill")
                        .resizable()
                        .scaledToFit()
                        .frame(width: 200, height: 200)
                        .foregroundColor(.white)
                    
                    // Completion Message
                    Text("Misión completada! Buen trabajo")
                        .font(.system(size: 35))
                        .fontWeight(.bold)
                        .multilineTextAlignment(.center)
                        .foregroundColor(.white)
                    
                    // Share Button
                    ZStack {
                        Button(action: {
                            isShareSheetPresented = true
                        }) {
                            HStack {
                                Image(systemName: "square.and.arrow.up")
                                Text("Compartir")
                                    .fontWeight(.bold)
                            }
                            .padding()
                            .background(Color(red: 245/255, green: 254/255, blue: 243/255))
                            .foregroundColor(.black)
                            .cornerRadius(10)
                            .shadow(radius: 5)
                        }
                    }
                    .padding(.horizontal)
                    
                    // Close Button
                    Button(action: {
                        isPresented = false
                    }) {
                        HStack {
                            Image(systemName: "xmark")
                            Text("Cerrar")
                                .fontWeight(.bold)
                        }
                        .padding()
                        .background(Color(red: 245/255, green: 254/255, blue: 243/255))
                        .foregroundColor(.black)
                        .cornerRadius(10)
                        .shadow(radius: 5)
                    }
                    .padding(.horizontal)
                    
                    Spacer()
                }
                .frame(maxWidth: .infinity, maxHeight: .infinity)
                .background(Color(red: 190/255, green: 220/255, blue: 116/255))
                .edgesIgnoringSafeArea(.all)
                .transition(.move(edge: .bottom))
                .animation(.easeInOut, value: isPresented)
            }
        }
        .fullScreenCover(isPresented: $isShareSheetPresented) {
            MissionShareView(completedMission: $completedMission)
        }
    }
}
