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
// More information of Gurux products: https://www.gurux.org
//
// This code is licensed under the GNU General Public License v2.
// Full text may be retrieved at http://www.gnu.org/licenses/gpl-2.0.txt
//---------------------------------------------------------------------------

package gurux.dlms.enums;

/**
 * Enumerable units.
 */
public enum Unit {
    /**
     * No Unit.
     */
    NONE(0),
    /**
     * Year.
     */
    YEAR(1),

    /**
     * Month.
     */
    MONTH(2),

    /**
     * Week.
     */
    WEEK(3),

    /**
     * Day.
     */
    DAY(4),

    /**
     * Hour.
     */
    HOUR(5),

    /**
     * Minute.
     */
    MINUTE(6),

    /**
     * Second.
     */
    SECOND(7),

    /**
     * Phase angle degree.
     */
    PHASE_ANGLE_DEGREE(8),
    /*
     * Temperature T degree centigrade, rad*180/p.
     */
    TEMPERATURE(9),
    /*
     * Local currency.
     */
    LOCAL_CURRENCY(10),
    /*
     * Length l meter m.
     */
    LENGTH(11),
    /*
     * Speed v m/s.
     */
    SPEED(12),
    /*
     * Volume V m3.
     */
    VOLUME_CUBIC_METER(13),
    /*
     * Corrected volume m3.
     */
    CORRECTED_VOLUME(14),
    /*
     * Volume flux m3/60*60s.
     */
    VOLUME_FLUX_HOUR(15),
    /*
     * Corrected volume flux m3/60*60s.
     */
    CORRECTED_VOLUME_FLUX_HOUR(16),
    /*
     * Volume flux m3/24*60*60s.
     */
    VOLUME_FLUX_DAY(17),
    /*
     * Corrected volume flux m3/24*60*60s.
     */
    CORRECTED_VOLUME_FLUX_DAY(18),
    /*
     * Volume 10-3 m3.
     */
    VOLUME_LITER(19),
    /*
     * Mass m kilogram kg.
     */
    MASS_KG(20),
    /*
     * return "Force F newton N.
     */
    FORCE(21),
    /*
     * Energy newtonmeter J = Nm = Ws.
     */
    ENERGY(22),
    /*
     * Pressure p pascal N/m2.
     */
    PRESSURE_PASCAL(23),
    /*
     * Pressure p bar 10-5 N/m2.
     */
    PRESSURE_BAR(24),
    /*
     * Energy joule J = Nm = Ws.
     */
    ENERGY_JOULE(25),
    /*
     * Thermal power J/60*60s.
     */
    THERMAL_POWER(26),
    /*
     * Active power P watt W = J/s.
     */
    ACTIVE_POWER(27),
    /*
     * Apparent power S.
     */
    APPARENT_POWER(28),
    /*
     * Reactive power Q.
     */
    REACTIVE_POWER(29),
    /*
     * Active energy W*60*60s.
     */
    ACTIVE_ENERGY(30),
    /*
     * Apparent energy VA*60*60s.
     */
    APPARENT_ENERGY(31),
    /*
     * Reactive energy var*60*60s.
     */
    REACTIVE_ENERGY(32),
    /*
     * Current I ampere A.
     */
    CURRENT(33),
    /*
     * Electrical charge Q coulomb C = As.
     */
    ELECTRICAL_CHARGE(34),
    /*
     * Voltage.
     */
    VOLTAGE(35),
    /*
     * Electrical field strength E V/m.
     */
    ELECTRICAL_FIELD_STRENGTH(36),
    /*
     * Capacity C farad C/V = As/V.
     */
    CAPACITY(37),
    /*
     * Resistance R ohm = V/A.
     */
    RESISTANCE(38),
    /*
     * Resistivity.
     */
    RESISTIVITY(39),
    /*
     * Magnetic flux F weber Wb = Vs.
     */
    MAGNETIC_FLUX(40),
    /*
     * Induction T tesla Wb/m2.
     */
    INDUCTION(41),
    /*
     * Magnetic field strength H A/m.
     */
    MAGNETIC(42),
    /*
     * Inductivity L henry H = Wb/A.
     */
    INDUCTIVITY(43),
    /*
     * Frequency f.
     */
    FREQUENCY(44),
    /*
     * Active energy meter constant 1/Wh.
     */
    ACTIVE(45),
    /*
     * Reactive energy meter constant.
     */
    REACTIVE(46),
    /*
     * Apparent energy meter constant.
     */
    APPARENT(47),
    /*
     * V260*60s.
     */
    V260(48),
    /*
     * A260*60s.
     */
    A260(49),
    /*
     * Mass flux kg/s.
     */
    MASS_KG_PER_SECOND(50),
    /*
     * Unit is Conductance siemens 1/ohm.
     */
    CONDUCTANCE(51),
    /*
     * Temperature in Kelvin.
     */
    KELVIN(52),
    /*
     * 1/(V2h) RU2h , volt-squared hour meter constant or pulse value.
     */
    RU2H(53),
    /*
     * 1/(A2h) RI2h , ampere-squared hour meter constant or pulse value.
     */
    RI2H(54),
    /*
     * 1/m3 RV , meter constant or pulse value (volume).
     */
    CUBIC_METER_RV(55),
    /*
     * Percentage.
     */
    PERCENTAGE(56),
    /*
     * Ah ampere hours.
     */
    AMPERE_HOURS(57),
    /*
     * Wh/m3 energy per volume 3,6*103 J/m3.
     */
    ENERGY_PER_VOLUME(60),
    /*
     * J/m3 calorific value, wobbe.
     */
    WOBBE(61),
    /*
     * Mol % molar fraction of gas composition mole percent (Basic gas
     * composition unit).
     */
    MOLE_PERCENT(62),
    /*
     * g/m3 mass density, quantity of material.
     */
    MASS_DENSITY(63),
    /*
     * Dynamic viscosity pascal second (Characteristic of gas stream).
     */
    PASCAL_SECOND(64),
    /*
     * J/kg Specific energy NOTE The amount of energy per unit of mass of a
     * substance Joule / kilogram m2 . kg . s -2 / kg = m2.
     */
    JOULE_KILOGRAM(65),
    /**
     * Pressure, gram per square centimeter.
     */
    PRESSURE_GRAM_PER_SQUARE_CENTIMETER(66),
    /**
     * Pressure, atmosphere.
     */
    PRESSURE_ATMOSPHERE(67),

    /*
     * Signal strength, dB milliwatt (e.g. of GSM radio systems).
     */
    SIGNAL_STRENGTH_MILLI_WATT(70),

    /**
     * Signal strength, dB microvolt.
     */
    SIGNAL_STRENGTH_MICRO_VOLT(71),
    /**
     * Logarithmic unit that expresses the ratio between two values of a
     * physical quantity
     */
    DB(72),
    /**
     * Length in inches.
     */
    INCH(128),
    /**
     * Foot (Length).
     */
    FOOT(129),
    /**
     * Pound (mass).
     */
    POUND(130),
    /**
     * Fahrenheit
     */
    FAHRENHEIT(131),
    /**
     * Rankine
     */
    RANKINE(132),
    /**
     * Square inch.
     */
    SQUARE_INCH(133),
    /**
     * Square foot.
     */
    SQUARE_FOOT(134),
    /**
     * Acre
     */
    ACRE(135),
    /**
     * Cubic inch.
     */
    CUBIC_INCH(136),
    /**
     * Cubic foot.
     */
    CUBIC_FOOT(137),
    /**
     * Acre-foot.
     */
    ACRE_FOOT(138),
    /**
     * Gallon (imperial).
     */
    GALLON_IMPERIAL(139),
    /**
     * Gallon (US).
     */
    GALLON_US(140),
    /**
     * Pound force.
     */
    POUND_FORCE(141),
    /**
     * Pound force per square inch
     */
    POUND_FORCE_PER_SQUARE_INCH(142),
    /**
     * Pound per cubic foot.
     */
    POUND_PER_CUBIC_FOOT(143),
    /**
     * Pound per (foot second)
     */
    POUND_PER_FOOT_SECOND(144),
    /**
     * Square foot per second.
     */
    SQUARE_FOOT_PER_SECOND(145),
    /**
     * British thermal unit.
     */
    BRITISH_THERMAL_UNIT(146),
    /**
     * Therm EU.
     */
    THERM_EU(147),
    /**
     * Therm US.
     */
    THERM_US(148),
    /**
     * British thermal unit per pound.
     */
    BRITISH_THERMAL_UNIT_PER_POUND(149),
    /**
     * British thermal unit per cubic foot.
     */
    BRITISH_THERMAL_UNIT_PER_CUBIC_FOOT(150),
    /**
     * Cubic feet.
     */
    CUBIC_FEET(151),
    /**
     * Foot per second.
     */
    FOOT_PER_SECOND(152),
    /**
     * Cubic foot per second.
     */
    CUBIC_FOOT_PER_SECOND(153),
    /**
     * Cubic foot per min.
     */
    CUBIC_FOOT_PER_MIN(154),
    /**
     * Cubic foot per hour.
     */
    CUBIC_FOOT_PER_HOUR(155),
    /**
     * Cubic foot per day
     */
    CUBIC_FOOT_PER_DAY(156),
    /**
     * Acre foot per second.
     */
    ACRE_FOOT_PER_SECOND(157),
    /**
     * Acre foot per min.
     */
    ACRE_FOOT_PER_MIN(158),
    /**
     * Acre foot per hour.
     */
    ACRE_FOOT_PER_HOUR(159),
    /**
     * Acre foot per day.
     */
    ACRE_FOOT_PER_DAY(160),
    /**
     * Imperial gallon.
     */
    IMPERIAL_GALLON(161),
    /**
     * Imperial gallon per second.
     */
    IMPERIAL_GALLON_PER_SECOND(162),
    /**
     * Imperial gallon per min.
     */
    IMPERIAL_GALLON_PER_MIN(163),
    /**
     * Imperial gallon per hour.
     */
    IMPERIAL_GALLON_PER_HOUR(164),
    /**
     * Imperial gallon per day.
     */
    IMPERIAL_GALLON_PER_DAY(165),
    /**
     * US gallon.
     */
    US_GALLON(166),
    /**
     * US gallon per second.
     */
    US_GALLON_PER_SECOND(167),
    /**
     * US gallon per min.
     */
    US_GALLON_PER_MIN(168),
    /**
     * US gallon per hour.
     */
    US_GALLON_PER_HOUR(169),
    /**
     * US gallon per day.
     */
    US_GALLON_PER_DAY(170),
    /**
     * British thermal unit per second.
     */
    BRITISH_THERMAL_UNIT_PER_SECOND(171),
    /**
     * British thermal unit per minute.
     */
    BRITISH_THERMAL_UNIT_PER_MINUTE(172),
    /**
     * British thermal unit per hour.
     */
    BRITISH_THERMAL_UNIT_PER_HOUR(173),
    /**
     * British thermal unit per day.
     */
    BRITISH_THERMAL_UNIT_PER_DAY(174),
    /*
     * Other Unit.
     */
    OTHER_UNIT(254),
    /*
     * No Unit.
     */
    NO_UNIT(255);

    private int intValue;
    private static java.util.HashMap<Integer, Unit> mappings;

    private static java.util.HashMap<Integer, Unit> getMappings() {
        synchronized (Unit.class) {
            if (mappings == null) {
                mappings = new java.util.HashMap<Integer, Unit>();
            }
        }
        return mappings;
    }

    Unit(final int value) {
        intValue = value;
        getMappings().put(value, this);
    }

    /*
     * Get integer value for enum.
     */
    public int getValue() {
        return intValue;
    }

    /*
     * Convert integer for enum value.
     */
    public static Unit forValue(final int value) {
        return getMappings().get(value);
    }

    // CHECKSTYLE:OFF
    @Override
    public String toString() {
        String str;
        switch (Unit.forValue(intValue)) {
        case NONE:
            str = "None";
            break;
        case YEAR:
            str = "Year";
            break;
        case MONTH:
            str = "Month";
            break;
        case WEEK:
            str = "Week";
            break;
        case DAY:
            str = "Day";
            break;
        case HOUR:
            str = "Hour";
            break;
        case MINUTE:
            str = "Minute";
            break;
        case SECOND:
            str = "Second";
            break;
        case PHASE_ANGLE_DEGREE:
            str = "PhaseAngle";
            break;
        case TEMPERATURE:
            str = "Temperature";
            break;
        case LOCAL_CURRENCY:
            str = "LocalCurrency";
            break;
        case LENGTH:
            str = "Length";
            break;
        case SPEED:
            str = "Speed";
            break;
        case VOLUME_CUBIC_METER:
            str = "Volume Cubic Meter";
            break;
        case CORRECTED_VOLUME:
            str = "Corrected volume";
            break;
        case VOLUME_FLUX_HOUR:
            str = "Volume flux hour";
            break;
        case CORRECTED_VOLUME_FLUX_HOUR:
            str = "Corrected volume flux hour";
            break;
        case VOLUME_FLUX_DAY:
            str = "Volume flux day";
            break;
        case CORRECTED_VOLUME_FLUX_DAY:
            str = "Corrected volume flux day";
            break;
        case VOLUME_LITER:
            str = "Volume liter";
            break;
        case MASS_KG:
            str = "Mass Kg";
            break;
        case FORCE:
            str = "Force";
            break;
        case ENERGY:
            str = "Energy";
            break;
        case PRESSURE_PASCAL:
            str = "Pressure pascal";
            break;
        case PRESSURE_BAR:
            str = "Pressure Bar";
            break;
        case ENERGY_JOULE:
            str = "Energy joule";
            break;
        case THERMAL_POWER:
            str = "Thermal power";
            break;
        case ACTIVE_POWER:
            str = "Active power";
            break;
        case APPARENT_POWER:
            str = "Apparent power";
            break;
        case REACTIVE_POWER:
            str = "Reactive power";
            break;
        case ACTIVE_ENERGY:
            str = "Active energy";
            break;
        case APPARENT_ENERGY:
            str = "Apparent energy";
            break;
        case REACTIVE_ENERGY:
            str = "Reactive energy";
            break;
        case CURRENT:
            str = "Current";
            break;
        case ELECTRICAL_CHARGE:
            str = "ElectricalCharge";
            break;
        case VOLTAGE:
            str = "Voltage";
            break;
        case ELECTRICAL_FIELD_STRENGTH:
            str = "Electrical field strength E V/m";
            break;
        case CAPACITY:
            str = "Capacity C farad C/V = As/V";
            break;
        case RESISTANCE:
            str = "Resistance";
            break;
        case RESISTIVITY:
            str = "Resistivity";
            break;
        case MAGNETIC_FLUX:
            str = "Magnetic flux F weber Wb = Vs";
            break;
        case INDUCTION:
            str = "Induction T tesla Wb/m2";
            break;
        case MAGNETIC:
            str = "Magnetic field strength H A/m";
            break;
        case INDUCTIVITY:
            str = "Inductivity L henry H = Wb/A";
            break;
        case FREQUENCY:
            str = "Frequency";
            break;
        case ACTIVE:
            str = "Active energy";
            break;
        case REACTIVE:
            str = "Reactive energy";
            break;
        case APPARENT:
            str = "Apparent energy";
            break;
        case V260:
            str = "V260*60s";
            break;
        case A260:
            str = "A260*60s";
            break;
        case MASS_KG_PER_SECOND:
            str = "Mass";
            break;
        case CONDUCTANCE:
            str = "Conductance siemens";
            break;
        case KELVIN:
            str = "Kelvin";
            break;
        case RU2H:
            str = "RU2h";
            break;
        case RI2H:
            str = "RI2h";
            break;
        case CUBIC_METER_RV:
            str = "Cubic meter RV";
            break;
        case PERCENTAGE:
            str = "Percentage";
            break;
        case AMPERE_HOURS:
            str = "Ampere hours";
            break;
        case ENERGY_PER_VOLUME:
            str = "Energy per volume";
            break;
        case WOBBE:
            str = "Wobbe";
            break;
        case MOLE_PERCENT:
            str = "Mole percent";
            break;
        case MASS_DENSITY:
            str = "Mass density";
            break;
        case PASCAL_SECOND:
            str = "Pascal second";
            break;
        case JOULE_KILOGRAM:
            str = "Joule kilogram";
            break;
        case PRESSURE_GRAM_PER_SQUARE_CENTIMETER:
            str = "Pressure, gram per square centimeter.";
            break;
        case PRESSURE_ATMOSPHERE:
            str = "Pressure, atmosphere.";
            break;
        case SIGNAL_STRENGTH_MILLI_WATT:
            str = "Signal strength, dB milliwatt";
            break;
        case SIGNAL_STRENGTH_MICRO_VOLT:
            // logarithmic unit that expresses the ratio between two values of a
            // physical quantity
            str = "Signal strength, dB microvolt";
            break;
        case DB:
            str = "dB";
            break;
        case INCH:
            str = "Inch";
            break;
        case FOOT:
            str = "Foot";
            break;
        case POUND:
            str = "Pound";
            break;
        case FAHRENHEIT:
            str = "Fahrenheit";
            break;
        case RANKINE:
            str = "Rankine";
            break;
        case SQUARE_INCH:
            str = "Square inch";
            break;
        case SQUARE_FOOT:
            str = "Square foot";
            break;
        case ACRE:
            str = "Acre";
            break;
        case CUBIC_INCH:
            str = "Cubic inch";
            break;
        case CUBIC_FOOT:
            str = "Cubic foot";
            break;
        case ACRE_FOOT:
            str = "Acre foot";
            break;
        case GALLON_IMPERIAL:
            str = "Gallon Imperial";
            break;
        case GALLON_US:
            str = "GallonUS";
            break;
        case POUND_FORCE:
            str = "Pound force";
            break;
        case POUND_FORCE_PER_SQUARE_INCH:
            str = "Pound force per square inch";
            break;
        case POUND_PER_CUBIC_FOOT:
            str = "Pound per cubic foot";
            break;
        case POUND_PER_FOOT_SECOND:
            str = "Pound per foot second";
            break;
        case BRITISH_THERMAL_UNIT:
            str = "British thermal unit";
            break;
        case THERM_EU:
            str = "Therm EU";
            break;
        case THERM_US:
            str = "Therm US";
            break;
        case BRITISH_THERMAL_UNIT_PER_POUND:
            str = "British thermal unit per pound";
            break;
        case BRITISH_THERMAL_UNIT_PER_CUBIC_FOOT:
            str = "British thermal unit per cubic foot";
            break;
        case CUBIC_FEET:
            str = "Cubic feet";
            break;
        case FOOT_PER_SECOND:
            str = "Foot per second";
            break;
        case CUBIC_FOOT_PER_MIN:
            str = "Foot per min";
            break;
        case CUBIC_FOOT_PER_DAY:
            str = "Foot per day";
            break;
        case ACRE_FOOT_PER_SECOND:
            str = "Acre foot per second";
            break;
        case ACRE_FOOT_PER_MIN:
            str = "Acre foot per min";
            break;
        case ACRE_FOOT_PER_HOUR:
            str = "Acre foot per hour";
            break;
        case ACRE_FOOT_PER_DAY:
            str = "Acre foot per day";
            break;
        case IMPERIAL_GALLON:
            str = "Imperial gallon";
            break;
        case IMPERIAL_GALLON_PER_SECOND:
            str = "Imperial gallon per second";
            break;
        case IMPERIAL_GALLON_PER_MIN:
            str = "Imperial gallon per min";
            break;
        case IMPERIAL_GALLON_PER_HOUR:
            str = "Imperial gallon per hour";
            break;
        case IMPERIAL_GALLON_PER_DAY:
            str = "Imperial gallon per day";
            break;
        case US_GALLON:
            str = "US Gallon";
            break;
        case US_GALLON_PER_SECOND:
            str = "US gallon per second";
            break;
        case US_GALLON_PER_MIN:
            str = "US gallon per min";
            break;
        case US_GALLON_PER_HOUR:
            str = "US gallon per hour";
            break;
        case US_GALLON_PER_DAY:
            str = "US gallon per day";
            break;
        case BRITISH_THERMAL_UNIT_PER_SECOND:
            str = "British thermal unit per second";
            break;
        case BRITISH_THERMAL_UNIT_PER_MINUTE:
            str = "British thermal unit per min";
            break;
        case BRITISH_THERMAL_UNIT_PER_HOUR:
            str = "British thermal unit per hour";
            break;
        case BRITISH_THERMAL_UNIT_PER_DAY:
            str = "British thermal unit per day";
            break;
        case OTHER_UNIT:
            str = "Other unit";
            break;
        case NO_UNIT:
            str = "NoUnit";
            break;
        default:
            str = "Unknown :" + String.valueOf(intValue);
        }
        return str;
    }
    // CHECKSTYLE:ON
}