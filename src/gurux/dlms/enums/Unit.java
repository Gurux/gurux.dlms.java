//
// --------------------------------------------------------------------------
//  Gurux Ltd
// 
//
//
// Filename:        $HeadURL$
//
// Version:         $Revision$,
//                  $Date$
//                  $Author$
//
// Copyright (c) Gurux Ltd
//
//---------------------------------------------------------------------------
//
//  DESCRIPTION
//
// This file is a part of Gurux Device Framework.
//
// Gurux Device Framework is Open Source software; you can redistribute it
// and/or modify it under the terms of the GNU General Public License 
// as published by the Free Software Foundation; version 2 of the License.
// Gurux Device Framework is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of 
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
// See the GNU General Public License for more details.
//
// More information of Gurux products: http://www.gurux.org
//
// This code is licensed under the GNU General Public License v2. 
// Full text may be retrieved at http://www.gnu.org/licenses/gpl-2.0.txt
//---------------------------------------------------------------------------

package gurux.dlms.enums;

public enum Unit 
{
    NONE(0),
    YEAR(1),
    MONTH(2),
    WEEK(3),
    DAY(4),
    HOUR(5),
    MINUTE(6),
    SECOND(7),
    PHASE_ANGLEGEGREE(8), // Phase angle degree rad*180/p
    TEMPERATURE(9), // Temperature T degree centigrade
    LOCAL_CURRENCY(10), //Local currency
    LENGTH(11), // Length l meter m
    SPEED(12), // "Speed v m/s
    VOLUME_CUBIC_METER(13), //Volume V m3
    CORRECTED_VOLUME(14), // Corrected volume m3
    VOLUME_FLUX_HOUR(15), //Volume flux m3/60*60s
    CORRECTED_VOLUME_FLUX_HOUR(16), // Corrected volume flux m3/60*60s
    VOLUME_FLUXDAY(17), // Volume flux m3/24*60*60s
    CORRECTE_VOLUME_FLUX_DAY(18), // Corrected volume flux m3/24*60*60s
    VOLUME_LITER(19), //Volume 10-3 m3
    MASS_KG(20), //Mass m kilogram kg
    FORCE(21), // return "Force F newton N
    ENERGY(22), // Energy newtonmeter J = Nm = Ws
    PRESSURE_PASCAL(23), // Pressure p pascal N/m2
    PRESSURE_BAR(24), // Pressure p bar 10-5 N/m2
    ENERGY_JOULE(25), // Energy joule J = Nm = Ws
    THERMAL_POWER(26), // Thermal power J/60*60s
    ACTIVE_POWER(27), //Active power P watt W = J/s
    APPARENT_POWER(28), // Apparent power S
    REACTIVE_POWER(29), //Reactive power Q
    ACTIVE_ENERGY(30), // Active energy W*60*60s
    APPARENT_ENERGY(31), // Apparent energy VA*60*60s
    REACTIVE_ENERGY(32), // Reactive energy var*60*60s
    CURRENT(33), // Current I ampere A
    ELECTRICAL_CHARGE(34), // Electrical charge Q coulomb C = As
    VOLTAGE(35), // Voltage
    ELECTRICAL_FIELD_STRENGTH(36), // Electrical field strength E V/m
    CAPACITY(37), // Capacity C farad C/V = As/V
    RESISTANCE(38), // Resistance R ohm = V/A
    RESISTIVITY(39), // Resistivity
    MAGNETIC_FLUX(40), // Magnetic flux F weber Wb = Vs
    INDUCTION(41), // Induction T tesla Wb/m2
    MAGNETIC(42), // Magnetic field strength H A/m
    INDUCTIVITY(43), // Inductivity L henry H = Wb/A
    FREQUENCY(44), // Frequency f
    ACTIVE(45), // Active energy meter constant 1/Wh
    REACTIVE(46), // Reactive energy meter constant
    APPARENT(47), // Apparent energy meter constant
    V260(48), // V260*60s
    A260(49), // A260*60s
    MASS_KG_PER_SECOND(50), // Mass flux kg/s
    
    /** 
    Unit is Conductance siemens 1/ohm.
   */
   CONDUCTANCE(51),
   /** 
    Temperature in Kelvin.
   */
   KELVIN(52),
   /** 
    1/(V2h) RU2h , volt-squared hour meter constant or pulse value.
   */
   V2H(53),
   /** 
    1/(A2h) RI2h , ampere-squared hour meter constant or pulse value.
   */
   A2H(54),
   /** 
    1/m3 RV , meter constant or pulse value (volume).
   */
   CUBIC_METER_RV(55),
   /** 
    Percentage
   */
   PERCENTAGE(56),
   // Ah ampere-hours 
   AMPERE_HOURS(57),
   /** 
    Wh/m3 energy per volume 3,6*103 J/m3.
   */
   ENERGY_PER_VOLUME(60),
   /** 
    J/m3 calorific value, wobbe.
   */
   WOBBE(61),
   /** 
    Mol % molar fraction of gas composition mole percent (Basic gas composition unit)
   */
   MOLE_PERCENT(62),
   /** 
    g/m3 mass density, quantity of material.
   */
   MASS_DENSITY(63),
   /** 
    Dynamic viscosity pascal second (Characteristic of gas stream).
   */
   PASCAL_SECOND(64),
   /** 
    J/kg Specific energy 
    NOTE The amount of energy per unit of mass of a 
    substance Joule / kilogram m2 . kg . s -2 / kg = m2 . s –2
   */
   JOULE_KILOGRAM(65),
   /** 
    dBm Signal strength (e.g. of GSM radio systems)
   */
   SIGNAL_STRENGTH(70),
   /*
    * Other Unit
    */
   OTHER_UNIT(254), 
   /*
    * No Unit
    */
    NO_UNIT(255); 

    private int intValue;
    private static java.util.HashMap<Integer, Unit> mappings;
    private static java.util.HashMap<Integer, Unit> getMappings()
    {
        synchronized (Unit.class)
        {
            if (mappings == null)
            {
                mappings = new java.util.HashMap<Integer, Unit>();
            }
        }
        return mappings;
    }

    @SuppressWarnings("LeakingThisInConstructor")
    private Unit(int value)
    {
        intValue = value;
        getMappings().put(value, this);
    }

    /*
     * Get integer value for enum.
     */
    public int getValue()
    {
        return intValue;
    }

    /*
     * Convert integer for enum value.
     */
    public static Unit forValue(int value)
    {
        return getMappings().get(value);
    }
    
    @Override
    public String toString()
    {
        String str;
        switch(intValue)
        {
            case 0: //NONE
                str = "None";
            break;
            case 1: //YEAR
            str = "Year";
                break;
            case 2: //MONTH
            str = "Month";
                break;
            case 3: //WEEK
            str = "Week";
                break;
            case 4: //DAY
            str = "Day";
                break;
            case 5: //HOUR
            str = "Hour";
            case 6: //MINUTE
            str = "Minute";
                break;
            case 7: //SECOND
            str = "Second";
                break;
            case 8: //PHASE_ANGLEGEGREE
            str = "PhaseAngle";
                break;
            case 9: //TEMPERATURE
            str = "Temperature";
                break;
            case 10: //LOCAL_CURRENCY
            str = "LocalCurrency";
                break;
            case 11: //LENGTH
            str = "Length";
                break;
            case 12: //SPEED
            str = "Speed";
                break;
            case 13: //VOLUME_CUBIC_METER
            str = "VolumeCubicMeter";
                break;
            case 14: //CORRECTED_VOLUME
            str = "CorrectededCVolume";
                break;
            case 15: //VOLUME_FLUX_HOUR
            str = "VolumeFluxHour";
                break;
            case 16: //CORRECTED_VOLUME_FLUX_HOUR
            str = "CorrectedVolumeFluxHour";
                break;
            case 17: //VOLUME_FLUXDAY
            str = "VolumeFluxDay";
                break;
            case 18: //CORRECTE_VOLUME_FLUX_DAY
            str = "CorrectedVolumeFluxDay";
                break;
            case 19: //VOLUME_LITER
            str = "VolumeLiter";
                break;
            case 20: //MASS_KG
            str = "MassKg";
                break;
            case 21: //FORCE
            str = "Force";
                break;
            case 22: //ENERGY
            str = "Energy";
                break;
            case 23: //PRESSURE_PASCAL
            str = "PressurePascal";
                break;
            case 24: //PRESSURE_BAR
            str = "PressureBar";
                break;
            case 25: //ENERGY_JOULE
            str = "EnergyJoule";
                break;
            case 26: //THERMAL_POWER
            str = "ThermalPower";
                break;
            case 27: //ACTIVE_POWER
            str = "ActivePower";
                break;
            case 28: //APPARENT_POWER
            str = "ApparentPower";
                break;
            case 29: //REACTIVE_POWER
            str = "ReactivePower";
                break;
            case 30: //ACTIVE_ENERGY
            str = "ActiveEnergy";
                break;
            case 31: //APPARENT_ENERGY
            str = "ApparentEenergy";
                break;
            case 32: //REACTIVE_ENERGY
            str = "ReactiveEnergy";
                break;
            case 33: //CURRENT
            str = "Current";
                break;
            case 34: //ELECTRICAL_CHARGE
            str = "ElectricalCharge";
                break;
            case 35: //VOLTAGE
            str = "Voltage";
                break;
            case 36: //ELECTRICAL_FIELD_STRENGTH
            str = "ElectricalFieldStrength";
                break;
            case 37: //CAPACITY
            str = "Capacity";
                break;
            case 38: //RESISTANCE
            str = "Resistance";
                break;
            case 39: //RESISTIVITY
            str = "Resistivity";
                break;
            case 40: //MAGNETIC_FLUX
            str = "MagneticFlux";
                break;                
            case 41: //INDUCTION
            str = "Induction";
                break;
            case 42: //MAGNETIC
            str = "Magnetic";
                break;
            case 43: //INDUCTIVITY
            str = "Inductivity";
                break;
            case 44: //FREQUENCY
            str = "Frequency";
                break;
            case 45: //ACTIVE
            str = "Active";
                break;
            case 46: //REACTIVE
            str = "Reactive";
                break;
            case 47: //APPARENT
            str = "Apparent";
                break;
            case 48: //V260
            str = "V260";
                break;
            case 49: //A260
            str = "A260";
                break;                
            case 50: //MASS_KG_PER_SECOND
            str = "MassKgPerSecond";
                break;
            case 51: //CONDUCTANCE
            str = "Conductance";
                break;
            case 52: //KELVIN
                str = "Kelvin.";
                break;
            case 53: //V2H
                str = "V2H";
                break;
            case 54: //A2H
                str = "A2H";
                break;
            case 55: //CUBIC_METER_RV
                str = "CubicMeterRV";
                break;
            case 56: //PERCENTAGE
                str = "Percentage";
                break;
            case 57: //AMPERE_HOURS
                str = "AmpereHours";
                break;
            case 60: //ENERGY_PER_VOLUME
                str = "EnergyPerVolume";
                break;
            case 61: //WOBBE
                str = "WOBBE";
                break;
            case 62: //MOLE_PERCENT
                str = "MolePercent";
                break;                
            case 63: //MASS_DENSITY
                str = "MassDensity";
                break;
            case 64: //PASCAL_SECOND
                str = "PascalSecond";
                break;
            case 65: //JOULE_KILOGRAM
                str = "JouleKilogram.";
                break;
            case 70: //SIGNAL_STRENGTH
                str = "SignalStrength";
                break;
            case 254: //OTHER_UNIT
                str = "OtherUnit";
                break;
            case 255: //NO_UNIT
                str = "NoUnit";
                break;
            default:
                str = "Unknown :" + String.valueOf(intValue);
        }
        return str;
    }
}