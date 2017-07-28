build:
	javac -d . src/*.java
	jar cfe OSRS_Loot_Simulator.jar Main *.class assets/
	rm -f *.class

app: OSRS_Loot_Simulator.jar
	javapackager -deploy -native -outdir application -outfile OSRS_Loot_Simulator -srcfiles OSRS_Loot_Simulator.jar -appclass Main -name "OSRS Loot Simulator" -title "OSRS Loot Simulator" -Bicon="assets/client/favicon.png"

exec:
	java -jar OSRS_Loot_Simulator.jar

clean:
	rm -rf OSRS_Loot_Simulator.jar application/*
