package agiletrack

class Task {
    String description
    Integer plannedPontuation
    Integer realPontuation

    static constraints = {
    }

    String toString(){
      return this.description
    }

}
