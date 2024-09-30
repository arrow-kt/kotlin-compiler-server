{ pkgs }: {
  deps = [
    pkgs.jdk17
    pkgs.jetbrains-jdk-jcef
    pkgs.graalvm17-ce
  ];
}