public class TwoFactor extends CoverBase {
	public static void approximation() {

	}

// APPROXIMATION-VERTEX-COVER(G)
// C = ∅
// E'= G.E

// while E' ≠ ∅:
//     pick a random edge in E' with vertices {u, v} 			| OP: pick random edge - O(1)/O(n)
//     add U, V to the cover															| OP: add edge - O(1)
//     remove any other edges connected to u or v from E'	| OP: O(n)
// return C
}
