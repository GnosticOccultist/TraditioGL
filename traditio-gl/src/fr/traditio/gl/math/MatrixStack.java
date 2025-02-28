package fr.traditio.gl.math;

import java.util.ArrayDeque;
import java.util.Deque;

public class MatrixStack {

	private final Deque<Matrix4f> stack = new ArrayDeque<>();

	public MatrixStack() {
		this.stack.push(new Matrix4f());
	}

	public void push() {
		stack.push(new Matrix4f(stack.peek()));
	}

	public void pop() {
		if (stack.size() > 1) {
			stack.pop();
		} else {
			System.err.println("MatrixStack: Cannot pop the last matrix!");
		}
	}

	public Matrix4f peek() {
		return stack.peek();
	}

	@Override
	public String toString() {
		return "MatrixStack [stack=" + stack + "]";
	}
}
