package ui;

import dataStructures.AdjacencyListGraph;
import model.MooreMachine;

import java.util.ArrayList;
import java.util.Scanner;

public class DesktopTest {
    public static void main(String[] args) {
        MooreMachine<Character, Boolean, Boolean> mooreMachine = new MooreMachine<>('A', false);
        mooreMachine.insertState('B', false);
        mooreMachine.insertState('C', false);
        mooreMachine.insertState('D', false);
        mooreMachine.insertState('E', false);
        mooreMachine.insertState('F', false);
        mooreMachine.insertState('G', false);
        mooreMachine.insertState('H', false);
        mooreMachine.insertState('I', true);
        mooreMachine.insertState('J', false);
        mooreMachine.insertState('K', true);

        mooreMachine.relate('A', 'B', false);
        mooreMachine.relate('A', 'A', true);
        mooreMachine.relate('B', 'C', false);
        mooreMachine.relate('B', 'D', true);
        mooreMachine.relate('C', 'E', false);
        mooreMachine.relate('C', 'C', true);
        mooreMachine.relate('D', 'F', false);
        mooreMachine.relate('D', 'B', true);
        mooreMachine.relate('E', 'G', false);
        mooreMachine.relate('E', 'E', true);
        mooreMachine.relate('F', 'H', false);
        mooreMachine.relate('F', 'F', true);
        mooreMachine.relate('G', 'I', false);
        mooreMachine.relate('G', 'G', true);
        mooreMachine.relate('H', 'J', false);
        mooreMachine.relate('H', 'H', true);
        mooreMachine.relate('I', 'A', false);
        mooreMachine.relate('I', 'K', true);
        mooreMachine.relate('J', 'K', false);
        mooreMachine.relate('J', 'J', true);
        mooreMachine.relate('K', 'A', false);
        mooreMachine.relate('K', 'K', true);

        Scanner s = new Scanner(System.in);
        System.out.println("******** Not minimized ********");
        String line = s.nextLine();
        while(line.length() == 1) {
            System.out.println(false + " " + mooreMachine.stateTransitionFunction(line.charAt(0), false));
            System.out.println(true + " " + mooreMachine.stateTransitionFunction(line.charAt(0), true));
            line = s.nextLine();
        }

        mooreMachine = mooreMachine.minimize();
        System.out.println("******** Minimized ********");
        line = s.nextLine();
        while(line.length() == 1) {
            System.out.println(false + " " + mooreMachine.stateTransitionFunction(line.charAt(0), false));
            System.out.println(true + " " + mooreMachine.stateTransitionFunction(line.charAt(0), true));
            line = s.nextLine();
        }
    }
}
