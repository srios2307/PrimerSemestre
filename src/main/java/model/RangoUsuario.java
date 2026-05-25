package model;

// Rangos del sistema de reputacion segun los puntos acumulados
public enum RangoUsuario {

    PRINCIPIANTE(0),
    INVERSIONISTA(100),
    EXPERTO_INMOBILIARIO(300),
    MAGNATE_INMOBILIARIO(600);

    private final int puntosMinimos;

    RangoUsuario(int puntosMinimos) {
        this.puntosMinimos = puntosMinimos;
    }

    public int getPuntosMinimos() {
        return puntosMinimos;
    }
}
