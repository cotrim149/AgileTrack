package agiletrack

class UserStorie {
    String title
    String description
    Integer pontuation

    static constraints = {
    }

    String toString(){
      return this.title
    }
}
