Morphone - a morphogenesis experiment.

"Morphone" is a reference to "morphogenesis through hormone-like
signaling", a term denoting the initial inspiration for this project
and which appears often throughout the code.

The purpose of this experiment is to investigate the properties of an
organism whose cells exchange hormone-like signals, as living cells do.

The organism consists of a cellular automaton in which each cell is
capable of secreting and absorbing hormones to and from its neighbors.
In each cell, morphogenic functions translate absorbed hormones into
changes in orientation, coloring, and subsequent hormone secretions.

Initially, all cells reside in a quiescent state, having the same color,
orientation, and secreting no hormones.  One cell is initially stimulated
by a secretion to begin the morphogenesis, causing the organism
to grow into patterns determined by its morphogenic functions.

All morphogens are extensions of the Morphogen class.
Included morphogens: Ball, Bug, Edge, FractalSpiral, GliderGun, Lsystem,
Spiral, SpiralingGliderGuns, Star, Timer, Transporter, and Turing.

Morphogens may also invoke other morphogens to perform useful functions,
such as transporting (Transport morphogen) and timing (Timer morphogen).
See Turing for an example.

Hormones (signals) for a morphogen are uniquely named by using the
ScopeGenerator and Scope classes. See any morphogen class for an
example.

Packages:

morphone.base: base classes.
morphone.samples: sample application classes.
morphone.misc: miscellaneous classes.

Documentation:

See doc and javadoc directories.

To build:

build.sh/build.bat

To run:

1. Applet:
a. Edit morphone.html and/or morphone_cheerpj.html to start desired morphogen (argument of Morphogen parameter, defaults to "Bug"):
   <applet code="morphone.base.Morphone.class" width=w height=h>
   <param name=Morphogen value="morphone.samples.<Morphogen class name>">
   </applet>
b. Start morphone.html in a browser or with appletviewer.

2. Application:
java -jar morphone.jar [-morphogen <Morphogen name>]

Reference:

Turing, A. M. (1952). "The Chemical Basis of Morphogenesis". Philosophical Transactions
of the Royal Society of London 237 (641): 37–72. doi:10.1098/rstb.1952.0012. JSTOR 92463

