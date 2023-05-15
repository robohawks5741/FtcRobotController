package org.firstinspires.ftc.teamcode.gamepadyn;

public class Mapping {
    public String description;
    public MappingActionDigital fu;
    public MappingActionDigital fd;
    public MappingActionDigital fl;
    public MappingActionDigital fr;
    public MappingActionDigital du;
    public MappingActionDigital dd;
    public MappingActionDigital dl;
    public MappingActionDigital dr;
    public MappingActionAnalog tr;
    public MappingActionAnalog tl;
    public MappingActionDigital br;
    public MappingActionDigital bl;
    public MappingActionAnalog sl;
    public MappingActionAnalog sr;
    public MappingActionDigital slb;
    public MappingActionDigital srb;

    public Mapping(IntermediateMapping im) {
        description = im.description;
        fu = im.maps.fu;
        fd = im.maps.fd;
        fl = im.maps.fl;
        fr = im.maps.fr;
        du = im.maps.du;
        dd = im.maps.fd;
        dl = im.maps.fl;
        dr = im.maps.fr;
        tl = im.maps.tl;
        tr = im.maps.tr;
        bl = im.maps.bl;
        br = im.maps.br;
        sl = im.maps.sl;
        sr = im.maps.sr;
        slb = im.maps.slb;
        srb = im.maps.srb;
    }
}
