package com.example.calculator.Data;

import com.example.calculator.Model.Measure;
import com.example.calculator.Model.Rates;
import com.example.calculator.StartUp;

public class ConvertData {
    static Measure length, area, mass, speed, data, volume, time, currency, temperature, angle;

    public static Measure getLength() {
        length = new Measure("Length");
        length.setUnits(new String[]{"Meter", "Kilometre", "Centimeter", "Foot", "Mile", "Inch", "Yard", "Decimeter", "Millimetre", "Micrometre", "Nanometre", " Picometre", "Nautical Mile", "Lunar Distance", "Astronomical Mile", "Light Year"});
        length.setFromMain(new double[]{1, 0.001, 100, 3.28084, 0.000621371, 39.3701, 1.09361, 10, 1000, 1000000, 1000000000, 1000000000000.0d, 0.000539957, 0.00000000260201912116327, 0.00000000000668458712, 0.00000000000000010570008340246155});
        length.setToMain(calculateToMain(length));
        return length;
    }

    public static Measure getMass() {
        mass = new Measure("Mass");
        mass.setUnits(new String[]{"Kilogram", "Gram", "Tonne", "Pound", "Ounce", "Milligram", "Microgram", "US Ton", "Quintal", "Imperial Ton", "Stone"});
        mass.setFromMain(new double[]{1, 1000, 0.001, 2.20462, 35.274, 1000000, 1000000000, 0.00110231, 0.01, 0.000984207, 0.157473119999996608});
        mass.setToMain(calculateToMain(mass));
        return mass;
    }

    public static Measure getArea() {
        area = new Measure("Area");
        area.setUnits(new String[]{"Square Meter", "Square Kilometre", "Acre", "Hectare", "Square Mile", "Square Yard", "Square Foot", "Square Inch"});
        area.setFromMain(new double[]{1, 0.000001, 0.000247105, 0.0001, 0.000000386102, 1.19599, 1550, 10.7639});
        area.setToMain(calculateToMain(area));
        return area;
    }

    public static Measure getAngle() {
        angle = new Measure("Angle");
        angle.setUnits(new String[]{"Degrees", "Radians", "Gradian", "Milliradian", "Minute of Arc", "Second of Arc"});
        angle.setFromMain(new double[]{1, Math.PI / 180, 1.11111, 17.4533, 60, 3600});
        angle.setToMain(calculateToMain(angle));
        return angle;
    }

    public static Measure getSpeed() {
        speed = new Measure("Speed");
        speed.setUnits(new String[]{"Meter per second", "Kilometer per hour", "Miles per hour", "Foot per second", "Knot"});
        speed.setFromMain(new double[]{1, 3.6, 0.621371, 0.911344, 0.539957});
        speed.setToMain(calculateToMain(speed));
        return speed;
    }

    public static Measure getData() {
        data = new Measure("Data");
        data.setUnits(new String[]{"Bit", "Kilobit", "Kibibit", "Megabit", "Mebibit", "Gigabit", "Gibibit", "Terabit", "Tebibit", "Petabit", "Pebibit", "Byte", "Kilobyte", "Kibibyte", "Megabyte", "Mebibyte", "Gigabyte", "Gibibyte", "Terabyte", "Tebibyte", "Petabyte", "Pebibyte"});
        data.setFromMain(new double[]{8000000, 8000, 7812.5, 8, 7.62939, 0.008, 1 / 134.0d, 0.000008, 1 / 137439.0d, 0.000000008, 1000000 / 140.73748836, 1000000, 1000, 976.563, 1, 0.953674, 0.001, 0.000931323, 0.000001, 0.000000909495, 0.000000001, 0.00000000088817842});
        data.setToMain(calculateToMain(data));
        return data;
    }

    public static Measure getTime() {
        time = new Measure("Time");
        time.setUnits(new String[]{"Nanosecond", "Microsecond", "Millisecond", "Second", "Minute", "Hour", "Day", "Week", "Month", "Calendar Year", "Decade", "Century"});
        time.setFromMain(new double[]{1000000000, 1000000, 1000, 1 / 60.0d, 1 / 3600.0d, 1 / 86400.0d, 1 / 604800.0d, 1 / (30.4167 * 1 / 86400), 1 / 3153600.0d, 1 / 31536000.0d, 1 / 315360000.0d});
        time.setToMain(calculateToMain(time));
        return time;
    }

    public static Measure getVolume() {
        volume = new Measure("Volume");
        volume.setUnits(new String[]{"US liquid gallon", "US liquid quart", "US liquid pint", "US legal cup", "fluid ounce", "US tablespoon", "US teaspoon", "Cubic meter", "Liter", "Milliliter", "Imperial gallon", "imp. quart", "Imperial pint", "Imperial cup", "fluid ounce", "Imperial tablespoon", "Imperial teaspoon", "Cubic foot", "Cubic inch"});
        volume.setFromMain(new double[]{264.172, 1056.69, 1 / 0.00024, 33814, 67628, 202884, 1, 1000, 1000000, 219.969, 879.877, 1759.75, 3519.51, 35195.1, 56312.1, 168936, 35.3147, 61023.7});
        volume.setToMain(calculateToMain(volume));
        return volume;
    }

    public static Measure getTemperature() {
        temperature = new Measure("Temperature");
        temperature.setUnits(new String[]{"Celsius", "Fahrenheit", "Kelvin"});
        temperature.setFromMain(new double[]{});
        temperature.setToMain(calculateToMain(temperature));
        return temperature;
    }

    public static Measure getCurrency() {
        Rates rates = StartUp.currentRates;
        currency = new Measure("Currency");
        currency.setUnits(new String[]{"AUD - Australian Dollar", "BGN - Bulgarian Lev", "BRL - Brazilian Rea", "CAD - Canadian Dollar", "CHF - Swiss Franc", "CNY - Chinese Yuan", "CZK - Czech Koruna", "DKK - Danish Krone", "GBP - British Pound", "HKD - Hong Kong Dollar", "HRK - Croatian Kuna", "HUF - Hungarian Forint", "IDR - Indonesian Rupiah", "ILS - Israeli New Shekel", "INR - Indian Rupee", "ISK - Icelandic Króna", "JPY - Japanese Yen", "KRW - South Korean Won", "MXN - Mexican Peso", "MYR - Malaysian Ringgit", "NOK - Norwegian Krone", "NZD - New Zealand Dollar", "PHP - Philippine Peso", "PLN - Polish Zloty", "RON - Romanian Leu", "RUB - Russian Ruble", "SEK - Swedish Krona", "SGD - Singapore Dollar", "THB - Thai Baht", "TRY - Turkish Lira", "USD - Us Dollar", "ZAR - South African Rand"});
        currency.setFromMain(new double[]{rates.getAUD(), rates.getBGN(), rates.getBRL(), rates.getCAD(), rates.getCHF(), rates.getCNY(), rates.getCZK(), rates.getDKK(), rates.getGBP(), rates.getHKD(), rates.getHRK(), rates.getHUF(), rates.getIDR(), rates.getILS(), rates.getINR(), rates.getISK(), rates.getJPY(), rates.getKRW(), rates.getMXN(), rates.getMYR(), rates.getNOK(), rates.getNZD(), rates.getPHP(), rates.getPLN(), rates.getRON(), rates.getRUB(), rates.getSEK(), rates.getSGD(), rates.getTHB(), rates.getTRY(), rates.getUSD(), rates.getZAR()});
        currency.setToMain(calculateToMain(currency));
        return currency;
    }


    private static double[] calculateToMain(Measure measure) {
        double[] toMain = new double[measure.getFromMain().length];
        for (int i = 0; i < measure.getFromMain().length; i++) {
            toMain[i] = 1 / measure.getFromMain()[i];
        }
        return toMain;
    }


}
