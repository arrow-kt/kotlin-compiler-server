{ pkgs }: {
    deps = [
        pkgs.corretto17
        pkgs.maven
        pkgs.replitPackages.jdt-language-server
        pkgs.replitPackages.java-debug
    ];
}