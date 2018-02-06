package org.usfirst.frc.team2811.robot;

/**
 * Table of pattern colors for the Blinken light controller, so that colors and 
 * patterns can be represented by named values.
 * <br>
 * Enum instances will have a 
 * {@link #us()} or {@link #pwm()}
 * method which returns an output value of the appropriate type
 *  
 * @author stormbots
 * @see manual {@linktourl https://www.revrobotics.com/content/docs/REV-11-1105-UM.pdf}
 */
public enum BlinkenPattern {	
	FIXED_RAINBOW_RAINBOW_PALETTE      (1005,-0.99),  //  PATTERN  1
	FIXED_RAINBOW_PARTY_PALETTE        (1015,-0.97),  //  PATTERN  2
	FIXED_RAINBOW_OCEAN_PALETTE        (1025,-0.95),  //  PATTERN  3
	FIXED_RAINBOW_LAVE_PALETTE         (1035,-0.93),  //  PATTERN  4
	RED_WHITE_STROBE                   (1045,-0.91),  //  PATTERN  5
	GREEN_WHITE_STROBE                 (1055,-0.89),  //  PATTERN  6
	FIXED_CONFETTI                     (1065,-0.87),  //  PATTERN  7
	FIXED_SHOT_RED                     (1075,-0.85),  //  PATTERN  8
	FIXED_SHOT_BLUE                    (1085,-0.83),  //  PATTERN  9
	FIXED_SHOT_WHITE                   (1095,-0.81),  //  PATTERN  10
	FIXED_SINELON_RAINBOW_PALETTE      (1105,-0.79),  //  PATTERN  11
	FIXED_SINELON_PARTY_PALETTE        (1115,-0.77),  //  PATTERN  12
	FIXED_SINELON_OCEAN_PALETTE        (1125,-0.75),  //  PATTERN  13
	FIXED_SINELON_LAVA_PALETTE         (1135,-0.73),  //  PATTERN  14
	FIXED_SINELON_FOREST_PALETTE       (1145,-0.71),  //  PATTERN  15
	FIXED_BPM_RAINBOW_PALETTE          (1155,-0.69),  //  PATTERN  16
	FIXED_BPM_PARTY_PALETTE            (1165,-0.67),  //  PATTERN  17
	FIXED_BPM_OCEAN_PALETTE            (1175,-0.65),  //  PATTERN  18
	BLUE_SHAKE                         (1185,-0.63),  //  PATTERN  19
	GREEN_SHAKE                        (1195,-0.61),  //  PATTERN  20
	FIXED_FIRE_MEDIUM                  (1205,-0.59),  //  PATTERN  21
	FIXED_FIRE_LARGE                   (1215,-0.57),  //  PATTERN  22
	FIXED_TWINKLES_RAINBOW_PALETTE     (1225,-0.55),  //  PATTERN  23
	FIXED_TWINKLES_PARTY_PALETTE       (1235,-0.53),  //  PATTERN  24
	BLUE_SPARKLING                     (1245,-0.51),  //  PATTERN  25
	FIXED_TWINKLES_LAVA_PALETTE        (1255,-0.49),  //  PATTERN  26
	FIXED_TWINKLES_FOREST_PALETTE      (1265,-0.47),  //  PATTERN  27
	FIXED_COLOR_WAVES_RAINBOW_PALETTE  (1275,-0.45),  //  PATTERN  28
	FIXED_COLOR_WAVES_PARTY_PALETTE    (1285,-0.43),  //  PATTERN  29
	WAVY_BLUE                          (1295,-0.41),  //  PATTERN  30
	FIXED_COLOR_WAVES_LAVA_PALETTE     (1305,-0.39),  //  PATTERN  31
	FIXED_COLOR_WAVES_FOREST_PALETTE   (1315,-0.37),  //  PATTERN  32
	FIXED_LARSON_SCANNER_RED           (1325,-0.35),  //  PATTERN  33
	FIXED_LARSON_SCANNER_GRAY          (1335,-0.33),  //  PATTERN  34
	FIXED_LIGHT_CHASE_RED              (1345,-0.31),  //  PATTERN  35
	BLUE_PULSE                         (1355,-0.29),  //  PATTERN  36
	FIXED_LIGHT_CHASE_GRAY             (1365,-0.27),  //  PATTERN  37
	FIXED_HEARTBEAT_RED                (1375,-0.25),  //  PATTERN  38
	FIXED_HEARTBEAT_BLUE               (1385,-0.23),  //  PATTERN  39
	FIXED_HEARTBEAT_WHITE              (1395,-0.21),  //  PATTERN  40
	FIXED_HEARTBEAT_GRAY               (1405,-0.19),  //  PATTERN  41
	FIXED_BREATH_RED                   (1415,-0.17),  //  PATTERN  42
	FIXED_BREATH_BLUE                  (1425,-0.15),  //  PATTERN  43
	FIXED_BREATH_GRAY                  (1435,-0.13),  //  PATTERN  44
	FIXED_STROBE_RED                   (1445,-0.11),  //  PATTERN  45
	FIXED_STROBE_BLUE                  (1455,-0.09),  //  PATTERN  46
	FIXED_STROBE_GOLD                  (1465,-0.07),  //  PATTERN  47
	FIXED_STROBE_WHITE                 (1475,-0.05),  //  PATTERN  48
	C1_END_TO_END_BLEND_TO_BLACK       (1485,-0.03),  //  PATTERN  49
	C1_LARSON_SCANNER                  (1495,-0.01),  //  PATTERN  50
	C1_LIGHT_CHASE                     (1505,0.01),   //  PATTERN  51
	C1_HEARTBEAT_SLOW                  (1515,0.03),   //  PATTERN  52
	C1_HEARTBEAT_MEDIUM                (1525,0.05),   //  PATTERN  53
	C1_HEARTBEAT_FAST                  (1535,0.07),   //  PATTERN  54
	C1_BREATH_SLOW                     (1545,0.09),   //  PATTERN  55
	C1_BREATH_FAST                     (1555,0.11),   //  PATTERN  56
	C1_SHOT                            (1565,0.13),   //  PATTERN  57
	C1_STROBE                          (1575,0.15),   //  PATTERN  58
	C2_END_TO_END_BLEND_TO_BLACK       (1585,0.17),   //  PATTERN  59
	C2_LARSON_SCANNER                  (1595,0.19),   //  PATTERN  60
	C2_LIGHT_CHASE                     (1605,0.21),   //  PATTERN  61
	C2_HEARTBEAT_SLOW                  (1615,0.23),   //  PATTERN  62
	C2_HEARTBEAT_MEDIUM                (1625,0.25),   //  PATTERN  63
	C2_HEARTBEAT_FAST                  (1635,0.27),   //  PATTERN  64
	C2_BREATH_SLOW                     (1645,0.29),   //  PATTERN  65
	C2_BREATH_FAST                     (1655,0.31),   //  PATTERN  66
	C2_SHOT                            (1665,0.33),   //  PATTERN  67
	C2_STROBE                          (1675,0.35),   //  PATTERN  68
	C12_SPARKLE_C1_ON_C2               (1685,0.37),   //  PATTERN  69
	C12_SPARKLE_C2_ON_C1               (1695,0.39),   //  PATTERN  70
	C12_COLOR_GRADIENT_C1_AND_C2       (1705,0.41),   //  PATTERN  71
	C12_BPM_C1_AND_C2                  (1715,0.43),   //  PATTERN  72
	C12_END_TO_END_BLEND_C1_TO_C2      (1725,0.45),   //  PATTERN  73
	C12_END_TO_END_BLEND_C2_TO_C1      (1735,0.47),   //  PATTERN  74   //TODO: Possibly incorrect description ; Documentation is odd here
	C12_END_TO_END_NO_BLEND            (1745,0.49),   //  PATTERN  75
	C12_TWINKLES                       (1755,0.51),   //  PATTERN  76
	C12_COLOR_WAVES                    (1765,0.53),   //  PATTERN  77
	C12_SINELON                        (1775,0.55),   //  PATTERN  78
	SOLID_HOT_PINK                     (1785,0.57),   //  PATTERN  79
	SOLID_DARK_RED                     (1795,0.59),   //  PATTERN  80
	SOLID_RED                          (1805,0.61),   //  PATTERN  81
	SOLID_RED_ORANGE                   (1815,0.63),   //  PATTERN  82
	SOLID_ORANGE                       (1825,0.65),   //  PATTERN  83
	SOLID_GOLD                         (1835,0.67),   //  PATTERN  84
	SOLID_YELLOW                       (1845,0.69),   //  PATTERN  85
	SOLID_LAWN_GREEN                   (1855,0.71),   //  PATTERN  86
	SOLID_LIME                         (1865,0.73),   //  PATTERN  87
	SOLID_DARK_GREEN                   (1875,0.75),   //  PATTERN  88
	SOLID_GREEN                        (1885,0.77),   //  PATTERN  89
	SOLID_BLUE_GREEN                   (1895,0.79),   //  PATTERN  90
	SOLID_BLUE                         (1905,0.81),   //  PATTERN  91
	SOLID_SKY_BLUE                     (1915,0.83),   //  PATTERN  92
	SOLID_DARK_BLUE                    (1925,0.85),   //  PATTERN  93
	SOLID_AQUA                         (1935,0.87),   //  PATTERN  94
	SOLID_BLUE_VIOLET                  (1945,0.89),   //  PATTERN  95
	SOLID_VIOLET                       (1955,0.91),   //  PATTERN  96
	SOLID_WHITE                        (1965,0.93),   //  PATTERN  97
	SOLID_GRAY                         (1975,0.95),   //  PATTERN  98
	SOLID_DARK_GRAY                    (1985,0.97),   //  PATTERN  99
	SOLID_BLACK                        (1995,0.99);   //  PATTERN  100
	

	private int us;
	private double pwm;
	
	BlinkenPattern(int us, double pwm){
		this.us = us;
		this.pwm = pwm;
	}
	
	/**
	 * For use with servo controller output, such as the SPARK
	 * @return pulse length in microseconds, between [1000..2000]
	 */
	public int us() {
		return this.us;
	}
	
	/**
	 * 
	 * @return pwm output, between [-1..1]
	 */
	public double pwm() {
		return this.pwm;
	}
}
