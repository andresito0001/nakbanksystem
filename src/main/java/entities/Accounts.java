package main.java.entities;

public enum Accounts {

    GASTOSVARIOS("628000", "Gastos Varios"),
    ELECTRIC_EQ_SPARES("604400", "Equipos electricos y repuestos"),
    TELECOM_EQ_SPARES("604600", "Equipos de telecomunicaciones y repuestos"), 
    CATER_ACCOM_SERV("605800", "Servicios de catering y alojamientos"), 
    TELECOM_SERV("605835", "Servicios de Telecomunicaciones"),
    GASOLINA("606210", "Gasolina y Diesel"), 
    ACEITES_LUBRICANTES("606250", "Aceites y lubricantes"),
    ARTICULOS_OFICINA("606400", "Articulos de oficina"),
    SOFTWARE_LICENCES("605000", "Licencias de software informatico"),
    COMP_EQ_SPARES("606520", "Equipos informaticos y repuestos"),
    COMP_HARDWARE_EQ("606540", "Equipos informaticos"),
    FOOD_DRINKS("606810", "Comidas y Bebidas"),
    ELECTRICITY("606110", "Servicios publicos - Electricidad"), 
    GAS("606120", "Servicios publicos - Gas"),
    WATER("606140", "Servicios publicos - Agua"),
    HOUSEHOLD_SUPPLIES("606450", "Suministros para el hogar"), 
    MEDICAL_SUPP("606820", "Suministros medicos y medicamentos"),
    BUILDING_RENTAL("613210", "Alquiler de Edificio"), 
    PARKING_RENTAL("613215", "Alquiler de Estacionamiento"),
    OFFICE_REPAIR("615230", "Reparaciones y mantenimiento - Oficinas"), 
    VEHICLE_SERV_REP("615600", "Servicio y reparacion de vehiculos"),
    TEMP_STAFF("621100", "Staff temporal"), 
    AUDIT_ACC_FEES("622610", "Honorarios de auditoria y contabilidad"), 
    LEGAL_FEES("622630", "Honorarios Legales"),
    MEDICAL_FEES("622640", "Honorarios medicos"),
    SUNDRY_FEES("622800", "Honorarios y Comisiones Varios"),
    BANK_TRANS_PM("635812", "Comision Pago Movil"),
    BANK_TRANS_TT("635813", "Comision Otros Bancos"),
    TELEPHONE("626200", "Telefono"),
    SALARY("641100", "Sueldos y Salarios"),
    ADMINISTRATIVE_TRANSPORT("624400", "Transporte Administrativo"),
    INTERESES("635815", "Intereses"),
    SUBSCRIPTIONS("628200", "Subscripciones"),
    UTILIDADES("641120", "Bono de aguinaldo"),
    BONUS("641300", "Bono"),
    TRAINING_TECH("622812", "Capacitación - Técnica / Profesional"),
    TRAINING_LENGUAJE("622814", "Capacitación - Lenguaje"),
    TRAINING_NO_TECH("622813", "Capacitación - No técnica"),
    ADV_PROMOTIONS("623100", "Publicidad y promociones"),
    TRAVEL_EXPENSES("625122", "Gastos de Viaje"),
    MOVING_EXPENSES("625500", "Gastos de mudanza"),
    VACATIONS_PAY("641200", "Pago de Vacaciones"),
    VACATIONS_BONUS("641220", "Bono de vacaciones"),
    CESTATICKETS("648400", "Tickets de Comida y Compensación"),
    DONACIONES("671300", "Donaciones y generosidades")
    ;

    Accounts(String costElementId, String costElementName) {
        this.costElementId = costElementId;
        this.costElementName = costElementName;
    }

    public String getCostElementId () { return costElementId; }
    public String getCostElementName () { return costElementName;}

    private String costElementId;
    private String costElementName;

}
