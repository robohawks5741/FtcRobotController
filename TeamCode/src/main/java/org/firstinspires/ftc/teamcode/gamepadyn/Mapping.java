package org.firstinspires.ftc.teamcode.gamepadyn;

public class Mapping {
    public String description;
    public MappingActionDigital fd;
    public MappingActionDigital fr;
    public MappingActionDigital fl;
    public MappingActionDigital fu;
    public MappingActionDigital du;
    public MappingActionDigital dd;
    public MappingActionDigital dl;
    public MappingActionDigital dr;
    public MappingActionAnalog tr;
    public MappingActionAnalog tl;
    public MappingActionDigital br;
    public MappingActionDigital bl;
    public MappingActionAnalog sr;
    public MappingActionDigital srb;
    public MappingActionAnalog sl;
    public MappingActionDigital slb;

    public Mapping(IntermediateMapping im) {
        description = im.description;
    }
}
