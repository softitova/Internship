// Задание 1
// Ответ: варианты d f g h
//
// Задание 2
// надо указать типы для HashMap,  иначе придется кастовать
// байтовый массив для ключа это плохо, так как при установлении соответствия придется пробегаться по байтивым массивам, а это долго.
// также возникаают проблемы при сравнении ключа, так как equals берется от объекта, то есть два одинаковых массива байт будут иметь разные ссылки и при сравнении будет неправильный результат
// Массивы не является неизменяемым  типом данных, для корректнй работы необходио сделать их immutable, иначе можно изменить значение элементов ключа и значения, и нарушить работу.
//
// Задание 3


public class Reverse {

    private static Node reverse(Node head) {
        Node curr = null;
        Node next = head;
        while (next != null) {
            Node temp = next.getNext();
            next.setNext(curr);
            curr = next;
            next = temp;
        }
        return curr;
    }
}

class Node {
    public int getPayload() {
        return payload;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    private int payload;
    private Node next;

    public Node(int payload, Node next) {
        this.payload = payload;
        this.next = next;
    }
}





