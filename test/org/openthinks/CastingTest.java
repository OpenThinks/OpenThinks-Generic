package org.openthinks;

public class CastingTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CastingTypeA a = new CastingTypeA();
		CastingTypeB b = new CastingTypeB();

		try {
			CastingType t = a;
			b = (CastingTypeB) t;
			if (b.getKey().equals(a.getValue())) {
				System.out.println("OK");
			}
		} catch (Exception e) {
			System.out.println("BAD");
		}

		CastingTypeAExtend ae = new CastingTypeAExtend();
		a = ae;

		System.out.println(a.getKey());

		ae = (CastingTypeAExtend) a;

		System.out.println(ae.getKey());
	}

}
