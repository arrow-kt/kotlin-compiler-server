{ pkgs }: {
    deps = [
        pkgs.corretto17
        pkgs.maven
    ];
    environment.sessionVariables = rec {
        ACCESS_CONTROL_ALLOW_ORIGIN_VALUE =	"*";
        ACCESS_CONTROL_ALLOW_HEADER_VALUE =	"*";
    };
}