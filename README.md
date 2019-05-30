[![Build Status](https://travis-ci.org/shapley-value-java/shapley-value-core.svg?branch=master)](https://travis-ci.org/shapley-value-java/shapley-value-core) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.shapley-value-java/shapley-value-core/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.github.shapley-value-java/shapley-value-core) 

Thanks to [![Structure101](http://structure101.com/static-content/images/s101_170.png)](https://structure101.com/)

# shapley-value-core
Shapley value calculation in Java

# Shapley value introduction


## Fair distribution 
* Efficiency
* Null player 
* Symetry
* Additivity

## Application example share a taxi 
How to share a taxi travel ?
Amy, Bob and Clare are sharing a taxi. We imagine they are going to the same direction.

- Amy must pay 6 to go home
- Bob must pay 12 to go home
- Clare must pay 42 to go home

If they share the taxi, the Shapley value will give :
- Amy must pay 2
- Bob must pay 5
- Clare must pay 35
- The total 2+5+35 = 42

With this example, we can see that Shapley value is for everybody efficient. Because Amy, Bob, and Clare pay less when they share than when they travel alone. 


# Calculation examples

Here is several examples with a few participants. You can imagine that with big groups the calculation becomes very difficult.

## One element
### Input 
characteristic function : <a href="https://www.codecogs.com/eqnedit.php?latex=2^{N}&space;\rightarrow&space;\mathbb{R}" target="_blank"><img src="https://latex.codecogs.com/gif.latex?2^{N}&space;\rightarrow&space;\mathbb{R}" title="2^{N} \rightarrow \mathbb{R}" /></a>

- <a href="https://www.codecogs.com/eqnedit.php?latex=v\left&space;(&space;\left&space;\{&space;1&space;\right&space;\}&space;\right&space;)&space;=&space;1.0" target="_blank"><img src="https://latex.codecogs.com/gif.latex?v\left&space;(&space;\left&space;\{&space;1&space;\right&space;\}&space;\right&space;)&space;=&space;1.0" title="v\left ( \left \{ 1 \right \} \right ) = 1.0" /></a>
### Output
shapley value 
<a href="https://www.codecogs.com/eqnedit.php?latex=\mathbb{N}\rightarrow&space;\mathbb{R},&space;\Phi&space;\left&space;(&space;1&space;\right&space;)=1.0" target="_blank"><img src="https://latex.codecogs.com/gif.latex?\mathbb{N}\rightarrow&space;\mathbb{R},&space;\Phi&space;\left&space;(&space;1&space;\right&space;)=1.0" title="\mathbb{N}\rightarrow \mathbb{R}, \Phi \left ( 1 \right )=1.0" /></a>

### Code example

```java
	@Test
	public void testCalculateOneParticipant() {
		
		CharacteristicFunction cfunction = 
				new CharacteristicFunction.CharacteristicFunctionBuilder(1)
				.addCoalition(1.0, 1).build();
	
		ShapleyValue s = new ShapleyValue(cfunction);
		s.calculate();
		Map<Integer,Double> output =s.getResult();
		
		double phi1 = output.get(1);	
		assertEquals(phi1, 1.0, 0.01);
	}
```

## Two elements
### Input
N = {1,2}
characteristic function : (2^N -> R)
- v({1})   = 1
- v({2})   = 2
- v({1,2}) = 4

### Calculation

|coalition | marginal contribution 1  | marginal contribution 2 |
| -------- | ------------------------ | ----------------------- |
| 1 12     | v({1})=1                 | v({1,2})-v({1}) =3      |
| 2 12     | v({1,2})-v({2})=2        | v({2})= 2               |

### Output
- phi1 = (1+2)/2=1.5
- phi2 = (3+2)/2=2.5

### Code example

```java
	@Test
	public void testCalculateTwoParticipants() {
		CharacteristicFunction cfunction = 
				new CharacteristicFunction.CharacteristicFunctionBuilder(2)
				.addCoalition(1.0, 1)		
				.addCoalition(2.0, 2)
				.addCoalition(4.0, 1, 2).build();	
			
		ShapleyValue s = new ShapleyValue(cfunction);		
		s.calculate();
		Map<Integer,Double> output =s.getResult();
		
		double phi1 = output.get(1);
		double phi2 = output.get(2);
		
		assertEquals(phi1, 1.5, 0.01);
		assertEquals(phi2, 2.5, 0.01);
		
	}
```

## Three elements
### Input
characteristic function : (2^N -> R)
- v({1})     = 80
- v({2})     = 56
- v({3})     = 70
- v({1,2})   = 80
- v({1,3})   = 85
- v({2,3})   = 72
- v({1,2,3}) = 90

### Output
- phi1 = 39.2
- phi2 = 20.7
- phi3 = 30.2

### Code example

```java
	@Test
	public void testCalculateThreeParticipants() {		
		CharacteristicFunction cfunction = 
				new CharacteristicFunction.CharacteristicFunctionBuilder(3)
				.addCoalition(80.0, 1)	
				.addCoalition(56.0, 2)
				.addCoalition(70.0, 3)	
				.addCoalition(80.0, 1, 2)			
				.addCoalition(85.0, 1, 3)		
				.addCoalition(72.0, 2, 3)			
				.addCoalition(90.0, 1, 2, 3).build();	
	
		ShapleyValue s = new ShapleyValue(cfunction);		
		s.calculate();
		Map<Integer,Double> output =s.getResult();
		
		double v1 = output.get(1);
		double v2 = output.get(2);
		double v3 = output.get(3);
		
		assertEquals(v1, 39.2, 0.1);
		assertEquals(v2, 20.7, 0.1);
		assertEquals(v3, 30.2, 0.1);		
	}
```

# Applications
## Share taxi
### Example
This example is :
Amy, Bob and Clare are sharing a taxi. We imagine they are going to the same direction.

- Amy must pay 6 to go home
- Bob must pay 12 to go home
- Clare must pay 42 to go home 

### Code example

```java
	@Test
	public void testCalculateThreeParticipants() {		
		TaxiApplication taxiApplication = 
				new TaxiApplication.TaxiApplicationBuilder()
				.addUser(6.0, "A")
				.addUser(12.0, "B")
				.addUser(42.0, "C")
				.build();

		Map<String,Double> output = taxiApplication.calculate();
		double phiA = output.get("A");
		double phiB = output.get("B");
		double phiC = output.get("C");
				
		assertEquals(phiA, 2.0, 0.01);
		assertEquals(phiB, 5.0, 0.01);
		assertEquals(phiC, 35.0, 0.01);	
	}
```
## Fraud rules evaluation
Example:
There are four fraudulent transactions T1, T2, T3, T4.
There are four rules trying to detect the fraud events.
- Rule1 detects T1, T2, T3
- Rule2 detects T1, T2, T3
- Rule3 detects T1, T2, T3
- Rule4 detects T4

The Shapley value evaluates the contribution of each rules (the sum will be normalized to 1)
The Rules4 detects 1/4 of the event (alone) so we can expect phiRule4=0.25
The rules 1, 2, 3 detect the same events and should have the same Shapley value.
phiRule1=phiRule2=phiRule2=0.25

### Code example

```java
	@Test
	public void testEvaluationFourRules() {
		
		FraudRuleApplication evaluation = 
				new FraudRuleApplication.FraudRuleApplicationBuilder()
				.addRule("Rule1", 1,2,3)
				.addRule("Rule2", 1,2,3)
				.addRule("Rule3", 1,2,3)
				.addRule("Rule4", 4)
				.build();
		
		Map<String,Double> output = evaluation.calculate();
		double phiRule1 = output.get("Rule1");
		double phiRule2 = output.get("Rule2");
		double phiRule3 = output.get("Rule3");
		double phiRule4 = output.get("Rule4");
		
		assertEquals(phiRule1, 0.25, 0.01);
		assertEquals(phiRule2, 0.25, 0.01);
		assertEquals(phiRule3, 0.25, 0.01);
		assertEquals(phiRule4, 0.25, 0.01);
	}
```

## Parliament
### First example
Question: The parliament of Micronesia is made up of four political parties, A, B, C, and D, which have 45, 25, 15, and 15 representatives, respectively.
If a coalition has the majority it receives 1, if a coalition has no majority it receives 0.

the solution is 
- phiA=0.5 
- phiB=1/6
- phiC=1/6
- phiD=1/6
B, C, and D have the same Shapley value, so the same influence.

### Belgium parliament
The Belgium parliament has 151 members and a lot of parties (13 parties, figures of the 18/04/2018)
- NVA 31
- PS 23
- MR 20
- CD&V 18
- openVLD 14
- PSA 13
- EcoloGroen 12
- CDH 9
- VB 3
- Defi 2
- PTB 2
- VW 2
- PP 1

In fact, it becomes very difficult to calculate the Shapley value for this 13 parties.
Because the number of permutation possible is huge : factorial(13)=  a lot around 6 milliard or 6 billion.

But it is possible to take a smaller set of permutations taken randomly.
In this case 500 000 permutations seems to be enough to have a precision of 0.1 percent and takes 30 seconds calculation with a old PC.

With a normalized results to 100, here is the result finally not so far from the proportional result
- NVA 22.9
- PS 15.4
- MR 13.5
- CD&V 12.0
- openVLD 8.8
- PSA 8.2
- EcoloGroen 7.6
- CDH 6.0
- VB 1.6
- Defi 1.1
- PTB 1.1
- VW 1.1
- PP 0.6

# Code quality
## Analysis with Struture101
![Structural overview](imgs/structure101-V1.png "Structural overview")

# Resources
## Video
* how to share a taxi https://www.youtube.com/watch?v=aThG4YAFErw
* course about game theory https://www.youtube.com/watch?v=qcLZMYPdpH4

## Example parliament application
https://math.stackexchange.com/questions/1310344/calculating-shapley-value-on-voting-game

## Other project in java
* http://bitsbytesnwords.blogspot.be/2013/02/shapley-value.html
