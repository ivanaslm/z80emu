package gorskima.z80emu;

import static gorskima.z80emu.Flag.H;
import static junitparams.JUnitParamsRunner.$;
import static gorskima.z80emu.Flag.N;
import static gorskima.z80emu.Flag.PV;
import static gorskima.z80emu.Flag.S;
import static gorskima.z80emu.Flag.Z;
import static gorskima.z80emu.Register.A;
import static gorskima.z80emu.Register.BC;
import static gorskima.z80emu.Register.HL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JUnitParamsRunner.class)
public class ALUTest {

	private Registers reg = new Registers();
	private ALU alu = new ALU(reg);

	@Test
	@Parameters
	public void testAdd(int op1, int op2, int result,
			boolean s, boolean z, boolean h, boolean pv, boolean c) {
		
		reg.setRegister(A, op1);
		alu.add(op2);
		assertThat(reg.getRegister(A), is(result));
		assertThat(reg.testFlag(S), is(s));
		assertThat(reg.testFlag(Z), is(z));
		assertThat(reg.testFlag(Flag.H), is(h));
		assertThat(reg.testFlag(PV), is(pv));
		assertThat(reg.testFlag(N), is(false)); // always
		assertThat(reg.testFlag(Flag.C), is(c));
	}
	
	private Object[] parametersForTestAdd() {
		return $(
			$(102, 38, 140, true, false, false, true, false));
	}

	@Test
	@Parameters
	public void testAdc(int op1, boolean carry, int op2, int result,
			boolean s, boolean z, boolean h, boolean pv, boolean c) {
		
		reg.setRegister(A, op1);
		reg.setFlag(Flag.C, carry);
		alu.adc(op2);
		assertThat(reg.getRegister(A), is(result));
		assertThat(reg.testFlag(S), is(s));
		assertThat(reg.testFlag(Z), is(z));
		assertThat(reg.testFlag(Flag.H), is(h));
		assertThat(reg.testFlag(PV), is(pv));
		assertThat(reg.testFlag(N), is(false)); // always
		assertThat(reg.testFlag(Flag.C), is(c));
	}
	
	private Object[] parametersForTestAdc() {
		return $(
			$(75, true, 200, 20, false, false, true, false, true));
	}

	@Test
	@Parameters
	public void testSub(int op1, int op2, int result,
			boolean s, boolean z, boolean h, boolean pv, boolean c) {
		
		reg.setRegister(A, op1);
		alu.sub(op2);
		assertThat(reg.getRegister(A), is(result));
		assertThat(reg.testFlag(S), is(s));
		assertThat(reg.testFlag(Z), is(z));
		assertThat(reg.testFlag(Flag.H), is(h));
		assertThat(reg.testFlag(PV), is(pv));
		assertThat(reg.testFlag(N), is(true)); // always
		assertThat(reg.testFlag(Flag.C), is(c));
	}
	
	private Object[] parametersForTestSub() {
		return $(
			$(80, 95, 241, true, false, true, false, true));
	}

	@Test
	@Parameters
	public void testSbc(int op1, boolean carry, int op2, int result,
			boolean s, boolean z, boolean h, boolean pv, boolean c) {
		
		reg.setRegister(A, op1);
		reg.setFlag(Flag.C, carry);
		alu.sbc(op2);
		assertThat(reg.getRegister(A), is(result));
		assertThat(reg.testFlag(S), is(s));
		assertThat(reg.testFlag(Z), is(z));
		assertThat(reg.testFlag(Flag.H), is(h));
		assertThat(reg.testFlag(PV), is(pv));
		assertThat(reg.testFlag(N), is(true)); // always
		assertThat(reg.testFlag(Flag.C), is(c));
	}
	
	private Object[] parametersForTestSbc() {
		return $(
			$(200, true, 100, 99, false, false, false, true, false));
	}

	@Test
	@Parameters
	public void testInc(int op, int result,
			boolean s, boolean z, boolean h, boolean pv) {
		
		reg.setRegister(A, op);
		alu.inc(A);
		assertThat(reg.getRegister(A), is(result));
		assertThat(reg.testFlag(S), is(s));
		assertThat(reg.testFlag(Z), is(z));
		assertThat(reg.testFlag(Flag.H), is(h));
		assertThat(reg.testFlag(PV), is(pv));
		assertThat(reg.testFlag(N), is(false)); // always
		// TODO check that C is unaffected
	}
	
	private Object[] parametersForTestInc() {
		return $(
			$(255, 0, false, true, true, false));
	}

	@Test
	@Parameters
	public void testDec(int op, int result,
			boolean s, boolean z, boolean h, boolean pv) {
		
		reg.setRegister(A, op);
		alu.dec(A);
		assertThat(reg.getRegister(A), is(result));
		assertThat(reg.testFlag(S), is(s));
		assertThat(reg.testFlag(Z), is(z));
		assertThat(reg.testFlag(Flag.H), is(h));
		assertThat(reg.testFlag(PV), is(pv));
		assertThat(reg.testFlag(N), is(true)); // always
		// TODO check that C is unaffected
	}

	private Object[] parametersForTestDec() {
		return $(
			$(0, 255, true, false, true, false));
	}
	
	@Test
	@Parameters
	public void testCp(int op1, int op2,
			boolean s, boolean z, boolean h, boolean pv, boolean c) {
		
		reg.setRegister(A, op1);
		alu.cp(op2);
		assertThat(reg.getRegister(A), is(op1)); // always
		assertThat(reg.testFlag(S), is(s));
		assertThat(reg.testFlag(Z), is(z));
		assertThat(reg.testFlag(Flag.H), is(h));
		assertThat(reg.testFlag(PV), is(pv));
		assertThat(reg.testFlag(N), is(true)); // always
		assertThat(reg.testFlag(Flag.C), is(c));
	}
	
	private Object[] parametersForTestCp() {
		return $(
			$(7, 5, false, false, false, false, false),
			$(8, 8, false, true, false, false, false));
	}

	@Test
	@Parameters
	public void testNeg(int op, int result,
			boolean s, boolean z, boolean h, boolean pv, boolean c) {
		
		reg.setRegister(A, op);
		alu.neg();
		assertThat(reg.getRegister(A), is(result));
		assertThat(reg.testFlag(S), is(s));
		assertThat(reg.testFlag(Z), is(z));
		assertThat(reg.testFlag(Flag.H), is(h));
		assertThat(reg.testFlag(PV), is(pv));
		assertThat(reg.testFlag(N), is(true)); // always
		assertThat(reg.testFlag(Flag.C), is(c));
	}
	
	private Object[] parametersForTestNeg() {
		return $(
			$(7, 249, true, false, true, false, true));
	}

	@Test
	@Parameters
	public void testAnd(int op1, int op2, int result,
			boolean s, boolean z, boolean pv) {
		
		reg.setRegister(A, op1);
		alu.and(op2);
		assertThat(reg.getRegister(A), is(result));
		assertThat(reg.testFlag(S), is(s));
		assertThat(reg.testFlag(Z), is(z));
		assertThat(reg.testFlag(Flag.H), is(true)); // always
		assertThat(reg.testFlag(PV), is(pv));
		assertThat(reg.testFlag(N), is(false)); // always
		assertThat(reg.testFlag(Flag.C), is(false)); // always
	}

	private Object[] parametersForTestAnd() {
		return $(
			$(70, 200, 64, false, false, false));
	}
	
	@Test
	@Parameters
	public void testOr(int op1, int op2, int result,
			boolean s, boolean z, boolean pv) {
		
		reg.setRegister(A, op1);
		alu.or(op2);
		assertThat(reg.getRegister(A), is(result));
		assertThat(reg.testFlag(S), is(s));
		assertThat(reg.testFlag(Z), is(z));
		assertThat(reg.testFlag(Flag.H), is(false)); // always
		assertThat(reg.testFlag(PV), is(pv));
		assertThat(reg.testFlag(N), is(false)); // always
		assertThat(reg.testFlag(Flag.C), is(false)); // always
	}
	
	private Object[] parametersForTestOr() {
		return $(
			$(70, 200, 206, true, false, false));
	}

	@Test
	@Parameters
	public void testXor(int op1, int op2, int result,
			boolean s, boolean z, boolean pv) {
		
		reg.setRegister(A, op1);
		alu.xor(op2);
		assertThat(reg.getRegister(A), is(result));
		assertThat(reg.testFlag(S), is(s));
		assertThat(reg.testFlag(Z), is(z));
		assertThat(reg.testFlag(Flag.H), is(false)); // always
		assertThat(reg.testFlag(PV), is(pv));
		assertThat(reg.testFlag(N), is(false)); // always
		assertThat(reg.testFlag(Flag.C), is(false)); // always
	}
	
	private Object[] parametersForTestXor() {
		return $(
			$(70, 200, 142, true, false, true));
	}

	@Test
	@Parameters
	public void testCpl(int op, int result) {
		reg.setRegister(A, op);
		alu.cpl();
		assertThat(reg.getRegister(A), is(result));
		assertThat(reg.testFlag(Flag.H), is(true)); // always
		assertThat(reg.testFlag(N), is(true)); // always
		// TODO check that S, Z, PV and C are unaffected
	}
	
	private Object[] parametersForTestCpl() {
		return $(
			$(3, 252));
	}
	
	@Test
	@Parameters
	public void testAdd16(int op1, int op2, int result,
			boolean c, boolean h) {
		
		reg.setRegister(HL, op1);
		alu.add16(op2);
		assertThat(reg.getRegister(HL), is(result));
		assertThat(reg.testFlag(Flag.C), is(c));
		assertThat(reg.testFlag(Flag.H), is(h));
		assertThat(reg.testFlag(N), is(false)); // always
		// TODO check that S, Z and PV are unaffected
	}
	
	private Object[] parametersForTestAdd16() {
		return $(
			$(40000, 30000, 4464, true, true));
	}

	@Test
	@Parameters
	public void testAdc16(int op1, boolean carry, int op2, int result,
			boolean s, boolean z, boolean h, boolean pv, boolean c) {
		
		reg.setRegister(HL, op1);
		reg.setFlag(Flag.C, carry);
		alu.adc16(op2);
		assertThat(reg.getRegister(HL), is(result));
		assertThat(reg.testFlag(Flag.S), is(s));
		assertThat(reg.testFlag(Flag.Z), is(z));
		assertThat(reg.testFlag(Flag.H), is(h));
		assertThat(reg.testFlag(Flag.PV), is(pv));
		assertThat(reg.testFlag(Flag.N), is(false)); // always
		assertThat(reg.testFlag(Flag.C), is(c));
	}
	
	private Object[] parametersForTestAdc16() {
		return $(
			$(30000, true, 35538, 3, false, false, true, false, true));
	}
	
	@Test
	@Parameters
	public void testSbc16(int op1, boolean carry, int op2, int result,
			boolean s, boolean z, boolean h, boolean pv, boolean c) {
		
		reg.setRegister(HL, op1);
		reg.setFlag(Flag.C, carry);
		alu.sbc16(op2);
		assertThat(reg.getRegister(HL), is(result));
		assertThat(reg.testFlag(Flag.S), is(s));
		assertThat(reg.testFlag(Flag.Z), is(z));
		assertThat(reg.testFlag(Flag.H), is(h));
		assertThat(reg.testFlag(Flag.PV), is(pv));
		assertThat(reg.testFlag(Flag.N), is(true)); // always
		assertThat(reg.testFlag(Flag.C), is(c));
	}
	
	private Object[] parametersForTestSbc16() {
		return $(
			$(40000, true, 20000, 19999, false, false, true, true, false));
	}
	
	@Test
	@Parameters
	public void testIncExtern(int op, int result,
			boolean s, boolean z, boolean h, boolean pv) {
		
		int incremented = alu.incExtern(op);
		assertThat(incremented, is(result));
		assertThat(reg.testFlag(S), is(s));
		assertThat(reg.testFlag(Z), is(z));
		assertThat(reg.testFlag(Flag.H), is(h));
		assertThat(reg.testFlag(PV), is(pv));
		assertThat(reg.testFlag(N), is(false)); // always
		// TODO check that C is unaffected
	}
	
	private Object[] parametersForTestIncExtern() {
		return $(
			$(8, 9, false, false, false, false));
	}
	
	@Test
	@Parameters
	public void testDecExtern(int op, int result,
			boolean s, boolean z, boolean h, boolean pv) {
		
		int decremented = alu.decExtern(op);
		assertThat(decremented, is(result));
		assertThat(reg.testFlag(S), is(s));
		assertThat(reg.testFlag(Z), is(z));
		assertThat(reg.testFlag(Flag.H), is(h));
		assertThat(reg.testFlag(PV), is(pv));
		assertThat(reg.testFlag(N), is(true)); // always
		// TODO check that C is unaffected
	}
	
	private Object[] parametersForTestDecExtern() {
		return $(
			$(128, 127, false, false, true, true));
	}
	
	@Test
	public void testInc16() {
		reg.setRegister(HL, 40000);
		alu.inc(HL);
		assertThat(reg.getRegister(HL), is(40001));
	}
	
	@Test
	public void testDec16() {
		reg.setRegister(BC, 40000);
		alu.dec(BC);
		assertThat(reg.getRegister(BC), is(39999));
	}
	
	@Test
	public void testRlca() {
		reg.setRegister(A, 0x81); // 10000001
		reg.setFlag(Flag.C, false);
		reg.setFlag(H, true);
		reg.setFlag(N, true);
		
		alu.rlca();
		assertThat(reg.getRegister(A), is(0x03)); // 00000011
		assertThat(reg.testFlag(Flag.C), is(true));
		assertThat(reg.testFlag(Flag.H), is(false));
		assertThat(reg.testFlag(Flag.N), is(false));
		
		alu.rlca();
		assertThat(reg.getRegister(A), is(0x06)); // 00000110
		assertThat(reg.testFlag(Flag.C), is(false));
		assertThat(reg.testFlag(Flag.H), is(false));
		assertThat(reg.testFlag(Flag.N), is(false));
	}
	
	@Test
	public void testRrca() {
		reg.setRegister(A, 0x02); // 00000010
		reg.setFlag(Flag.C, false);
		reg.setFlag(H, true);
		reg.setFlag(N, true);
		
		alu.rrca();
		assertThat(reg.getRegister(A), is(0x01)); // 00000001
		assertThat(reg.testFlag(Flag.C), is(false));
		assertThat(reg.testFlag(Flag.H), is(false));
		assertThat(reg.testFlag(Flag.N), is(false));
		
		alu.rrca();
		assertThat(reg.getRegister(A), is(0x80)); // 10000000
		assertThat(reg.testFlag(Flag.C), is(true));
		assertThat(reg.testFlag(Flag.H), is(false));
		assertThat(reg.testFlag(Flag.N), is(false));
	}
	
	@Test
	public void testRla() {
		reg.setFlag(Flag.C, false);
		reg.setRegister(A, 0x85); // 10000101
		reg.setFlag(H, true);
		reg.setFlag(N, true);
		
		alu.rla();
		assertThat(reg.getRegister(A), is(0x0A)); // 00001010
		assertThat(reg.testFlag(Flag.C), is(true));
		assertThat(reg.testFlag(Flag.H), is(false));
		assertThat(reg.testFlag(Flag.N), is(false));
		
		alu.rla();
		assertThat(reg.getRegister(A), is(0x15)); // 00010101
		assertThat(reg.testFlag(Flag.C), is(false));
		assertThat(reg.testFlag(Flag.H), is(false));
		assertThat(reg.testFlag(Flag.N), is(false));
	}
	
	@Test
	public void testRra() {
		reg.setFlag(Flag.C, true);
		reg.setRegister(A, 0x06); // 00000110
		reg.setFlag(H, true);
		reg.setFlag(N, true);
		
		alu.rra();
		assertThat(reg.getRegister(A), is(0x83)); // 10000011
		assertThat(reg.testFlag(Flag.C), is(false));
		assertThat(reg.testFlag(Flag.H), is(false));
		assertThat(reg.testFlag(Flag.N), is(false));
		
		alu.rra();
		assertThat(reg.getRegister(A), is(0x41)); // 01000001
		assertThat(reg.testFlag(Flag.C), is(true));
		assertThat(reg.testFlag(Flag.H), is(false));
		assertThat(reg.testFlag(Flag.N), is(false));
	}

}
