package agiletrack

class AcceptanceCriteria {

    String description
    boolean accepted

    static constraints = {
    }

    String toString(){
      return this.description
    }
}
