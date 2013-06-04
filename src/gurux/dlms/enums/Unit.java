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
    CORRECT_ED_VOLUME(14), // Corrected volume m3
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
    CONDUCTANCE(51), // Conductance siemens 1/ohm
    OTHER_UNIT(254), // Other Unit
    NO_UNIT(255); // No Unit

    private int intValue;
    private static java.util.HashMap<Integer, Unit> mappings;
    private static java.util.HashMap<Integer, Unit> getMappings()
    {
        synchronized (Unit.class)
        {
            if (mappings == null)
            {
                mappings = new java.util.HashMap<>();
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
}