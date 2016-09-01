
/**
 * An object that stores the rectangular arguments of complex numbers and
 * provides a means to perform the majority of mathematical operations on
 * these numbers while maintaining the algebraic and numerical properties
 * of complex numbers
 * 
 * @author Joshua Fehrenbach
 *
 */
public class Complex
{
	private static final long serialVersionUID = 6308821956046582233L;

	private final double a, b;

	public static final Complex ONE = new Complex(1.0, 0.0);
	public static final Complex I = new Complex(0.0, 1.0);
	public static final Complex NEGATIVE_ONE = new Complex(-1.0, 0.0);
	public static final Complex NEGATIVE_I = new Complex(0.0, -1.0);

	/**
	 * Creates a new Complex Number object from the rectangular points
	 * ({@code real},&nbsp;{@code imaginary})
	 * 
	 * @param real		The real coefficient of a complex number
	 * @param imaginary	The imaginary coefficient of a complex number
	 */
	public Complex(double real, double imaginary)
	{
		if(real != real) a = 0.0;
		else a = real;

		if(imaginary != imaginary) b = 0.0;
		else b = imaginary;
	}

	/** Creates a Complex Number object that represents the numerical value of 0 */
	public Complex()
	{
		a = 0.0;
		b = 0.0;
	}

	/**
	 * Adds the complex number <i>z</i> to this complex number
	 * 
	 * @param z	The complex number to add to this object
	 * @return	{@code this}&nbsp;+&nbsp;z
	 */
	public Complex add(Complex z)
	{
		return new Complex(a + z.a, b + z.b);
	}

	/**
	 * Adds this complex number with the negative of the complex number <i>z</i>
	 * <p>
	 * Equivalent to (<code>this&nbsp;+&nbsp;(-z)</code>)
	 * 
	 * @param z	The complex number to subtract from this object
	 * @return	{@code this}&nbsp;-&nbsp;z
	 */
	public Complex subtract(Complex z)
	{
		return new Complex(a - z.a, b - z.b);
	}

	/**
	 * Multiplies this complex number by the complex number <i>z</i>
	 * 
	 * @param z	The complex number to multiply with this object
	 * @return	{@code this}&times;z
	 */
	public Complex multiply(Complex z)
	{
		return new Complex(a*z.a - b*z.b, a*z.b + b*z.a);
	}

	/**
	 * Divides this complex number by the complex number <i>z</i>
	 * <p>
	 * Equivalent to (<code>this&times;(z<sup>-1</sup>)</code>)
	 * 
	 * @param z	The complex number to divide this object by
	 * @return	{@code this}&div;z
	 */
	public Complex divide(Complex z)
	{
		double a0 = z.a/(z.a*z.a + z.b*z.b);
		double b0 = -z.b/(z.a*z.a + z.b*z.b);

		return new Complex(a*a0 - b*b0, a*b0 + b*a0);
	}

	/**
	 * Gives the complex number conjugate of this object
	 * <p>
	 * A complex number in the form of: a&nbsp;+&nbsp;b<i>i</i> would return: a&nbsp;-&nbsp;b<i>i</i>
	 * 
	 * @return	The complex number conjugate of this object
	 */
	public Complex getConjugate()
	{
		return new Complex(a, -b);
	}

	/**
	 * Gives the multiplicative inverse of this number
	 * <p>
	 * For a complex number a&nbsp;+&nbsp;b<i>i</i> returns the complex
	 * number value of 1&div;(a&nbsp;+&nbsp;b<i>i</i>)
	 * <p>
	 * This is equivalent to
	 * <ul>
	 * (a&nbsp;-&nbsp;b<i>i</i>)&div;(a<sup>2</sup>&nbsp;+&nbsp;b<sup>2</sup>)
	 * </ul>
	 * or
	 * <ul>
	 * (a&div;(a<sup>2</sup>&nbsp;+&nbsp;b<sup>2</sup>))&nbsp;-&nbsp;<i>i</i>(b&div;(a<sup>2</sup>&nbsp;+&nbsp;b<sup>2</sup>))
	 * </ul>
	 * 
	 * @return	1&div;this
	 */
	public Complex getReciprocal()
	{
		double a0 = a/(a*a + b*b);
		double b0 = -b/(a*a + b*b);

		return new Complex(a0, b0);
	}

	/**
	 * Return the value of this object raised to the power of another complex number,
	 * as a complex number
	 * 
	 * @param power	The complex number to raise this object to the power of
	 * @return		this<sup>power</sup>
	 */
	public Complex pow(Complex power)
	{
		double logThis = 0.5*Math.log(a*a + b*b);
		double th = arg();
		double mag = Math.exp(power.a*logThis - power.b*th);
		th = power.b*logThis + power.a*th;

		return new Complex(mag*Math.cos(th), mag*Math.sin(th));
	}

	/**
	 * Gives the sine of this complex number, treating this number
	 * as a complex angle
	 * <p>
	 * Uses the identity
	 * <ul>
	 * sin(a+b<i>i</i>)&nbsp;=&nbsp;sin(a)cosh(b)&nbsp;+&nbsp;<i>i</i>cos(a)sinh(b)
	 * </ul>
	 * 
	 * @return	<i>sin</i>(this)
	 */
	public Complex sin()
	{
		return new Complex(Math.sin(a)*Math.cosh(b), Math.cos(a)*Math.sinh(b));
	}

	/**
	 * Gives the cosine of this complex number, treating this number
	 * as a complex angle
	 * <p>
	 * Uses the identity
	 * <ul>
	 * cos(a+b<i>i</i>)&nbsp;=&nbsp;cos(a)cosh(b)&nbsp;-&nbsp;<i>i</i>sin(a)sinh(b)
	 * </ul>
	 * 
	 * @return	<i>cos</i>(this)
	 */
	public Complex cos()
	{
		return new Complex(Math.cos(a)*Math.cosh(b), -Math.sin(a)*Math.sinh(b));
	}

	/**
	 * Gives the tangent of this complex number, treating this number
	 * as a complex angle
	 * <p>
	 * Uses the identity
	 * <ul>
	 * tan(a+b<i>i</i>)&nbsp;=&nbsp;(sin(a)cos(a)&nbsp;+&nbsp;<i>i</i>sinh(b)cosh(b))&nbsp;&div;&nbsp;(cos<sup>2</sup>(a)&nbsp;+&nbsp;sinh<sup>2</sup>(b))
	 * </ul>
	 * 
	 * @return	<i>tan</i>(this)
	 */
	public Complex tan()
	{
		double mul = 1.0 / (Math.cos(2*a) + Math.cosh(2*b));
		return new Complex(mul * Math.sin(2*a), mul * Math.sinh(2*b));
	}

	/**
	 * Gives the cosecant of this complex number, treating this number
	 * as a complex angle
	 * <p>
	 * Uses the identity
	 * <ul>
	 * csc(a+b<i>i</i>)&nbsp;=&nbsp;(2sin(a)cosh(b)&nbsp;-&nbsp;2<i>i</i>cos(a)sinh(b))&nbsp;&div;&nbsp;(cosh(2b)&nbsp;-&nbsp;cos(2a))
	 * </ul>
	 * 
	 * @return	<i>csc</i>(this)
	 */
	public Complex csc()
	{
		double mul = 2.0 / (Math.cosh(b*2) - Math.cos(a*2));
		return new Complex(Math.cosh(b)*Math.sin(a)*mul, -Math.cos(a)*Math.sinh(b)*mul);
	}

	/**
	 * Gives the secant of this complex number, treating this number
	 * as a complex angle
	 * <p>
	 * Uses the identity
	 * <ul>
	 * sec(a+b<i>i</i>)&nbsp;=&nbsp;(2cos(a)cosh(b)&nbsp;+&nbsp;2<i>i</i>sin(a)sinh(b))&nbsp;&div;&nbsp;(cos(2a)&nbsp;+&nbsp;cosh(2b))
	 * </ul>
	 * 
	 * @return	<i>sec</i>(this)
	 */
	public Complex sec()
	{
		double mul = 2.0 / (Math.cos(a*2) + Math.cosh(b*2));
		return new Complex(Math.cos(a)*Math.cosh(b)*mul, Math.sin(a)*Math.sinh(b)*mul);
	}

	/**
	 * Gives the cotangent of this complex number, treating this number
	 * as a complex angle
	 * <p>
	 * Uses the identity
	 * <ul>
	 * cot(a+b<i>i</i>)&nbsp;=&nbsp;(sin(2a)&nbsp;-&nbsp;<i>i</i>sinh(2b))&nbsp;&div;&nbsp;(cosh(2b)&nbsp;-&nbsp;cos(2a))
	 * </ul>
	 * 
	 * @return	<i>cot</i>(this)
	 */
	public Complex cot()
	{
		double mul = 1.0 / (Math.cosh(2*b) - Math.cos(2*a));
		return new Complex(mul * Math.sin(2*a), -mul * Math.sinh(2*b));
	}

	/**
	 * Takes the arc sine of this complex number, giving it as a complex angle
	 * <p>
	 * Uses the definition of arc sine, which is
	 * <ul>
	 * arcsin(x)&nbsp;=&nbsp;-<i>i</i>log<sub><i>e</i></sub>(<i>i</i>x&nbsp;+&nbsp;&radic;(1&nbsp;-&nbsp;x<sup>2</sup>))
	 * </ul>
	 * 
	 * @return	<i>arcsin</i>(this)
	 */
	public Complex arcsin()
	{
		double a, b;
		a = 1 - (this.a*this.a - this.b*this.b);
		b = -2*this.a*this.b;

		double r = Math.pow(a*a + b*b, 0.25);
		double t = Math.atan2(b, a) / 2.0;

		a = Math.cos(t)*r - this.b;
		b = Math.sin(t)*r + this.a;

		return new Complex(Math.atan2(b, a), -0.5*Math.log(a*a + b*b));
	}

	/**
	 * Takes the arc cosine of this complex number, giving it as a complex angle
	 * <p>
	 * Uses the definition of arc cosine, which is
	 * <ul>
	 * arccos(x)&nbsp;=&nbsp;-<i>i</i>log<sub><i>e</i></sub>(x&nbsp;+&nbsp;&radic;(x<sup>2</sup>&nbsp;-&nbsp;1))
	 * </ul>
	 * 
	 * @return	<i>arccos</i>(this)
	 */
	public Complex arccos()
	{
		double a, b;
		a = this.a*this.a - this.b*this.b - 1;
		b = 2*this.a*this.b;

		double r = Math.pow(a*a + b*b, 0.25);
		double t = Math.atan2(b, a) / 2.0;

		a = r*Math.cos(t) + this.a;
		b = r*Math.sin(t) + this.b;

		return new Complex(Math.atan2(b, a), -0.5*Math.log(a*a + b*b));
	}

	/**
	 * Takes the arc tangent of this complex number, giving it as a complex angle
	 * <p>
	 * Uses the definition of arc tangent, which is
	 * <ul>
	 * arctan(x)&nbsp;=&nbsp;-(<i>i</i>&div;2)*(log<sub>e</sub>(<i>i</i>-x)&nbsp;-&nbsp;log<sub>e</sub>(<i>i</i>+x))
	 * </ul>
	 * 
	 * @return	<i>arctan</i>(this)
	 */
	public Complex arctan()
	{
		double r1 = a*a + 1 - 2*b + b*b;
		double r2 = a*a + 1 + 2*b + b*b;
		double t1 = Math.atan2(1-b, -a);
		double t2 = Math.atan2(1+b,  a);

		return new Complex((t1 - t2) / 2.0, (Math.log(r1) - Math.log(r2)) / 4.0);
	}

	/**
	 * Takes the arc cosecant of this complex number, giving it as a complex angle
	 * <p>
	 * Uses the definition of arc cosecant, which is
	 * <ul>
	 * arccsc(x)&nbsp;=&nbsp;arcsin(x<sup>-1</sup>)
	 * </ul>
	 * 
	 * @return	<i>arccsc</i>(this)
	 */
	public Complex arccsc()
	{
		return getReciprocal().arcsin();
	}

	/**
	 * Takes the arc secant of this complex number, giving it as a complex angle
	 * <p>
	 * Uses the definition of arc secant, which is
	 * <ul>
	 * arcsec(x)&nbsp;=&nbsp;arccos(x<sup>-1</sup>)
	 * </ul>
	 * 
	 * @return	<i>arcsec</i>(this)
	 */
	public Complex arcsec()
	{
		return getReciprocal().arccos();
	}

	/**
	 * Takes the arc cotangent of this complex number, giving it as a complex angle
	 * <p>
	 * Uses the definition of arc cotangent, which is
	 * <ul>
	 * arccot(x)&nbsp;=&nbsp;arctan(x<sup>-1</sup>)
	 * </ul>
	 * 
	 * @return	<i>arccot</i>(this)
	 */
	public Complex arccot()
	{
		return getReciprocal().arctan();
	}

	/**
	 * Gives the hyperbolic sine of this complex number, treating
	 * this number as a complex hyperbolic angle
	 * <p>
	 * Uses the identity
	 * <ul>
	 * sinh(a+b<i>i</i>)&nbsp;=&nbsp;sinh(a)cos(b)&nbsp;+&nbsp;<i>i</i>cosh(a)sin(b)
	 * </ul>
	 * 
	 * @return	<i>sinh</i>(this)
	 */
	public Complex sinh()
	{
		return new Complex(Math.sinh(a)*Math.cos(b), Math.cosh(a)*Math.sin(b));
	}

	/**
	 * Gives the hyperbolic cosine of this complex number, treating
	 * this number as a complex hyperbolic angle
	 * <p>
	 * Uses the identity
	 * <ul>
	 * cosh(a+b<i>i</i>)&nbsp;=&nbsp;cosh(a)cos(b)&nbsp;+&nbsp;<i>i</i>sinh(a)sin(b)
	 * </ul>
	 * 
	 * @return	<i>cosh</i>(this)
	 */
	public Complex cosh()
	{
		return new Complex(Math.cosh(a)*Math.cos(b), Math.sinh(a)*Math.sin(b));
	}

	/**
	 * Gives the hyperbolic tangent of this complex number, treating
	 * this number as a complex hyperbolic angle
	 * <p>
	 * Uses the identity
	 * <ul>
	 * tanh(a+b<i>i</i>)&nbsp;=&nbsp;(sinh(2a)&nbsp;+&nbsp;<i>i</i>sin(2b))&nbsp;&div;&nbsp;(cosh(2a)&nbsp;+&nbsp;cos(2b))
	 * </ul>
	 * 
	 * @return	<i>tanh</i>(this)
	 */
	public Complex tanh()
	{
		double mul = 1.0 / (Math.cosh(2*a) + Math.cos(2*b));
		return new Complex(mul * Math.sinh(2*a), mul * Math.sin(2*b));
	}

	/**
	 * Gives the hyperbolic cosecant of this complex number, treating
	 * this number as a complex hyperbolic angle
	 * <p>
	 * Uses the identity
	 * <ul>
	 * csch(a+b<i>i</i>)&nbsp;=&nbsp;(2sinh(a)cos(b)&nbsp;-&nbsp;2<i>i</i>cosh(a)sin(b))&nbsp;&div;&nbsp;(cosh(2a)&nbsp;-&nbsp;cos(2b))
	 * </ul>
	 * 
	 * @return	<i>csch</i>(this)
	 */
	public Complex csch()
	{
		double mul = 2.0 / (Math.cosh(2*a) - Math.cos(2*b));
		return new Complex(mul * Math.sinh(a)*Math.cos(b), -mul * Math.sin(b)*Math.cosh(a));
	}

	/**
	 * Gives the hyperbolic secant of this complex number, treating
	 * this number as a complex hyperbolic angle
	 * <p>
	 * Uses the identity
	 * <ul>
	 * sech(a+b<i>i</i>)&nbsp;=&nbsp;(2cosh(a)cos(b)&nbsp;-&nbsp;2<i>i</i>sinh(a)sin(b))&nbsp;&div;&nbsp;(cosh(2a)&nbsp;+&nbsp;cos(2b))
	 * </ul>
	 * 
	 * @return	<i>sech</i>(this)
	 */
	public Complex sech()
	{
		double mul = 2.0 / (Math.cosh(2*a) + Math.cos(2*b));
		return new Complex(mul * Math.cosh(a)*Math.cos(b), -mul * Math.sinh(a)*Math.sin(b));
	}

	/**
	 * Gives the hyperbolic cotangent of this complex number, treating
	 * this number as a complex hyperbolic angle
	 * <p>
	 * Uses the identity
	 * <ul>
	 * coth(a+b<i>i</i>)&nbsp;=&nbsp;(sinh(2a)&nbsp;-&nbsp;<i>i</i>sin(2b))&nbsp;&div;&nbsp;(cosh(2a)&nbsp;-&nbsp;cos(2b))
	 * </ul>
	 * 
	 * @return	<i>coth</i>(this)
	 */
	public Complex coth()
	{
		double mul = 1.0 / (Math.cosh(2*a) - Math.cos(2*b));
		return new Complex(mul * Math.sinh(2*a), -mul * Math.sin(2*b));
	}

	/**
	 * Takes the hyperbolic arc sine of this complex number, giving
	 * it as a complex hyperbolic angle
	 * <p>
	 * Uses the definition of hyperbolic arc sine, which is
	 * <ul>
	 * arcsinh(x)&nbsp;=&nbsp;log<sub><i>e</i></sub>(x&nbsp;+&nbsp;&radic;(x<sup>2</sup>+1))
	 * </ul>
	 * 
	 * @return	<i>arcsinh</i>(this)
	 */
	public Complex arcsinh()
	{
		double a, b;
		a = this.a*this.a - this.b*this.b + 1;
		b = 2*this.a*this.b;

		double r = Math.pow(a*a + b*b, 0.25);
		double t = Math.atan2(b, a) / 2.0;

		a = this.a + r*Math.cos(t);
		b = this.b + r*Math.sin(t);

		return new Complex(0.5 * Math.log(a*a + b*b), Math.atan2(b, a));
	}

	/**
	 * Takes the hyperbolic arc cosine of this complex number, giving
	 * it as a complex hyperbolic angle
	 * <p>
	 * Uses the definition of hyperbolic arc cosine, which is
	 * <ul>
	 * arccosh(x)&nbsp;=&nbsp;log<sub><i>e</i></sub>(x&nbsp;+&nbsp;&radic;(x<sup>2</sup>-1))
	 * </ul>
	 * 
	 * @return	<i>arccosh</i>(this)
	 */
	public Complex arccosh()
	{
		double a, b;
		a = this.a*this.a - this.b*this.b + 1;
		b = 2*this.a*this.b;

		double r = Math.pow(a*a + b*b, 0.25);
		double t = Math.atan2(b, a) / 2.0;

		a = this.a + r*Math.cos(t);
		b = this.b + r*Math.sin(t);

		return new Complex(0.5 * Math.log(a*a + b*b), Math.atan2(b, a));
	}

	/**
	 * Takes the hyperbolic arc tangent of this complex number, giving
	 * it as a complex hyperbolic angle
	 * <p>
	 * Uses the definition of hyperbolic arc tangent, which is
	 * <ul>
	 * arctanh(x)&nbsp;=&nbsp;(log<sub><i>e</i></sub>(1+x)&nbsp;-&nbsp;log<sub><i>e</i></sub>(1-x))&div;2
	 * </ul>
	 * 
	 * @return	<i>arctanh</i>(this)
	 */
	public Complex arctanh()
	{
		double r1 = a*a + 2*a + 1 + b*b;
		double r2 = a*a - 2*a + 1 + b*b;
		double t1 = Math.atan2(b, 1 + a);
		double t2 = Math.atan2(b, 1 - a);

		return new Complex((Math.log(r1) - Math.log(r2)) / 4.0, (t1 - t2) / 2.0);
	}

	/**
	 * Takes the hyperbolic arc cosecant of this complex number, giving
	 * it as a complex hyperbolic angle
	 * <p>
	 * Uses the definition of hyperbolic arc cosecant, which is
	 * <ul>
	 * arccsch(x)&nbsp;=&nbsp;arcsinh(x<sup>-1</sup>)
	 * </ul>
	 * 
	 * @return <i>arccsch</i>(this)
	 */
	public Complex arccsch()
	{
		return getReciprocal().arcsinh();
	}

	/**
	 * Takes the hyperbolic arc secant of this complex number, giving
	 * it as a complex hyperbolic angle
	 * <p>
	 * Uses the definition of hyperbolic arc secant, which is
	 * <ul>
	 * arcsech(x)&nbsp;=&nbsp;arccosh(x<sup>-1</sup>)
	 * </ul>
	 * 
	 * @return <i>arcsech</i>(this)
	 */
	public Complex arcsech()
	{
		return getReciprocal().arccosh();
	}

	/**
	 * Takes the hyperbolic arc cotangent of this complex number, giving
	 * it as a complex hyperbolic angle
	 * <p>
	 * Uses the definition of hyperbolic arc cotangent, which is
	 * <ul>
	 * arccoth(x)&nbsp;=&nbsp;arctanh(x<sup>-1</sup>)
	 * </ul>
	 * 
	 * @return <i>arccoth</i>(this)
	 */
	public Complex arccoth()
	{
		return getReciprocal().arctanh();
	}

	/**
	 * Gives the real part of this complex number
	 * <p>
	 * A complex number in the form of "a + b<i>i</i>" will return "a"
	 * 
	 * @return	The real part of this complex number
	 */
	public double Re()
	{
		return a;
	}

	/**
	 * Gives the imaginary part of this complex number
	 * <p>
	 * A complex number in the form of "a + b<i>i</i>" will give "b"
	 * 
	 * @return	The imaginary part of this complex number
	 */
	public double Im()
	{
		return b;
	}

	/**
	 * Gives the absolute value, or magnitude, of this complex number
	 * <p>
	 * Will always return a number greater than or equal to 0
	 * <ul>
	 * <p>
	 * A complex number in the form of "a + b<i>i</i>" will give the square root of
	 * a<sup>2</sup> + b<sup>2</sup>
	 * <p>
	 * A complex number in the form of "r&times;<i>e</i><sup><i>i</i>&times;
	 * <i>theta</i></sup>" will give "r"
	 * </ul>
	 * 
	 * @return	The magnitude of this complex number
	 */
	public double abs()
	{
		return Math.hypot(a, b);
	}

	/**
	 * Gives the angle of the polar form of this complex number
	 * <ul>
	 * <p>
	 * A complex number in the form of "a + b<i>i</i>" will give arctan(b/a)
	 * <p>
	 * A complex number in the form of "r&times;<i>e</i><sup><i>i</i>&times;
	 * <i>theta</i></sup>" will give "<i>theta</i>"
	 * </ul>
	 * 
	 * @return	The angle of the polar form of this complex number
	 */
	public double arg()
	{
		return Math.atan2(b, a);
	}

	/**
	 * Exponentiates this complex number by setting it as <i>e</i><sup>this</sup>
	 * 
	 * @return	<i>e</i><sup>this</sup>
	 */
	public Complex exp()
	{
		double mag = Math.exp(a);
		return new Complex(mag*Math.cos(b), mag*Math.sin(b));
	}

	/**
	 * Exponentiaties this complex number by setting is as <i>z</i><sup>this</sup>
	 * 
	 * @param z	The base of the exponentiation function, as a complex number
	 * @return	<i>z</i><sup>this</sup>
	 */
	public Complex exp(Complex z)
	{
		return z.pow(this);
	}

	/**
	 * Returns the natural logarithm of this complex number
	 * 
	 * @return	ln(this)
	 */
	public Complex ln()
	{
		return new Complex(Math.log(a*a + b*b) / 2.0, Math.atan2(b, a));
	}

	/**
	 * Returns log<sub>z</sub> of this complex number
	 * 
	 * @return	ln(this)
	 */
	public Complex ln(Complex z)
	{
		return ln().divide(z.ln());
	}

	/**
	 * Gives a complex number object from the specified polar coordinates
	 * 
	 * @param r		Magnitude of the values
	 * @param theta	Angle of the values
	 * @return		The rectangular form from these polar coordinates
	 * @throws IllegalArgumentException If a negative value or "r" is given
	 */
	public static Complex getFromPolar(double r, double theta)
	{
		if(r == 0) return new Complex();
		if(r < 0) throw new IllegalArgumentException("Cannot have a negative magnitude: " + r);
		if(r != r || Double.isInfinite(r)) throw new IllegalArgumentException("Invalid Magnitude: " + r);
		if(theta != theta || Double.isInfinite(theta)) throw new IllegalArgumentException("Invalid Angle: " + theta);

		return new Complex(r*Math.cos(theta), r*Math.sin(theta));
	}
}
