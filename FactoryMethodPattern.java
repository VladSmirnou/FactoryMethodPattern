// Let's say that I have a system that process user bills ->

class InputDto {
    String billType;
    int moneyAmount;
    int debt;

    public InputDto(String b, int m, int d) {
        billType = b;
        moneyAmount = m;
        debt = d;
    }
}

class OutputDto {
    boolean billIsPayed;
    
    public OutputDto(boolean p) {
        billIsPayed = p;
    }
    
    @Override
    public String toString() {
        return billIsPayed ? "Bill is payed!" : "Not enought money!";
    }
}

class BillSystem {
    public static OutputDto processBill(InputDto inp) {
        OutputDto obj = null;
        if (inp.billType.equals("Internet")) {
            IntegnetBill bill = new IntegnetBill();
            obj = new OutputDto(bill.payBill(inp.moneyAmount, inp.debt));
        } else if (inp.billType.equals("Mobile")) {
            MobileBill bill = new MobileBill();
            obj = new OutputDto(bill.payBill(inp.moneyAmount, inp.debt));
        }
        return obj;
    }
}

class MobileBill {
    public boolean payBill(int moneyAmount, int debt) {
        return moneyAmount >= debt;
    }
}

class IntegnetBill {
    public boolean payBill(int moneyAmount, int debt) {
        return moneyAmount >= debt;
    }
}

class EntryPoint {
    public static void main(String[] args) {
        InputDto obj = new InputDto("Mobile", 22, 23);
        System.out.print(BillSystem.processBill(obj));
    }
}

// This implementation works, but doesn't follow the OCP, because, maybe, 
// this system will be able to process more than just 'Internet' and 'Mobile' bills
// in the future. Maybe the company policy will change and it will stop
// processing 'Mobile' bills, etc. In those cases I'd need to expand or reduce that 'if'
// check in method 'processBill'.

// I can try to fix this problem using the method factory pattern. The method factory
// pattern uses an abstract class in its core, which is very stable yet extendable without
// any source code modification.

class InputDto {
    private int moneyAmount;
    private int debt;
    
    public InputDto(int m, int d) {
        moneyAmount = m;
        debt = d;
    }

    public int getMoneyAmount() {
        return moneyAmount;
    }

    public int getDebt() {
        return debt;
    }
}

class OutputDto {
    Bill billObject;
    
    public OutputDto(Bill billObject) {
        this.billObject = billObject;
    }

    @Override
    public String toString() {
        String templ = billObject.getBillStatus() ? "%s bill is payed!" : "Not enought money to pay the %s bill!";
        String btype = billObject.getBillType();
        return String.format(templ, btype);
    }
}

abstract class BillSystem {
    public Bill processBill(InputDto inputDtoObj) {
        Bill bill = makeBill();
        bill.payBill(inputDtoObj);
        return bill;
    }

    abstract public Bill makeBill();
}

class MobileBillCreator extends BillSystem {

    @Override
    public Bill makeBill() {
        return new MobileBillProduct();
    }
}

class InternetBillCreator extends BillSystem {

    @Override
    public Bill makeBill() {
        return new IntegnetBillProduct();
    }
}

interface Bill {
    boolean getBillStatus();
    String getBillType();
    void payBill(InputDto inputDtoObj);
}

class MobileBillProduct implements Bill {
    private boolean billStatus;
    private final String billType = "Mobile";

    @Override
    public void payBill(InputDto inputDtoObj) {
        billStatus = (
            inputDtoObj.getMoneyAmount() >= inputDtoObj.getDebt()
        );
    }

    @Override
    public boolean getBillStatus() {
        return billStatus;
    }
    
    @Override
    public String getBillType() {
        return billType;
    }
}

class IntegnetBillProduct implements Bill {
    private boolean billStatus;
    private final String billType = "Internet";

    @Override
    public void payBill(InputDto inputDtoObj) {
        billStatus = (
            inputDtoObj.getMoneyAmount() >= inputDtoObj.getDebt()
        );
    }

    @Override
    public boolean getBillStatus() {
        return billStatus;
    }

    @Override
    public String getBillType() {
        return billType;
    }
}

class EntryPoint {
    public static void main(String[] args) {
        InputDto mobileBillData = new InputDto(25, 23);
        InputDto internetBillData = new InputDto(22, 23);
        
        BillSystem mobileBillCreator = new MobileBillCreator();
        BillSystem internetBillCreator = new InternetBillCreator();
        
        Bill mobileBill = mobileBillCreator.processBill(mobileBillData);
        Bill internetBill = internetBillCreator.processBill(internetBillData);
        
        OutputDto mobileBillStatus = new OutputDto(mobileBill);
        OutputDto internetBillStatus = new OutputDto(internetBill);
        
        System.out.println(mobileBillStatus);
        System.out.println(internetBillStatus);
    }
}

// Another solution using generics. 
// Even tho generics are not about object creation at all,
// they also help to work with types of objects that don't
// yet exist. This solution is also applicable in this case ->

class InputDto {
    private int moneyAmount;
    private int debt;
    
    public InputDto(int m, int d) {
        moneyAmount = m;
        debt = d;
    }

    public int getMoneyAmount() {
        return moneyAmount;
    }

    public int getDebt() {
        return debt;
    }
}

class OutputDto {
    Bill billObject;
    
    public OutputDto(Bill billObject) {
        this.billObject = billObject;
    }

    @Override
    public String toString() {
        String templ = billObject.getBillStatus() ? "%s bill is payed!" : "Not enought money to pay the %s bill!";
        String btype = billObject.getBillType();
        return String.format(templ, btype);
    }
}

class BillSystem {
    public static <T extends Bill> void processBill(
            T billImpl, InputDto inputDtoObj) 
    {
        billImpl.payBill(inputDtoObj);
    }
}

interface Bill {
    boolean getBillStatus();
    String getBillType();
    void payBill(InputDto inputDtoObj);
}

class MobileBillProduct implements Bill {
    private boolean billStatus;
    private final String billType = "Mobile";

    @Override
    public void payBill(InputDto inputDtoObj) {
        billStatus = (
            inputDtoObj.getMoneyAmount() >= inputDtoObj.getDebt()
        );
    }

    @Override
    public boolean getBillStatus() {
        return billStatus;
    }
    
    @Override
    public String getBillType() {
        return billType;
    }
}

class IntegnetBillProduct implements Bill {
    private boolean billStatus;
    private final String billType = "Internet";

    @Override
    public void payBill(InputDto inputDtoObj) {
        billStatus = (
            inputDtoObj.getMoneyAmount() >= inputDtoObj.getDebt()
        );
    }

    @Override
    public boolean getBillStatus() {
        return billStatus;
    }

    @Override
    public String getBillType() {
        return billType;
    }
}

class EntryPoint {
    public static void main(String[] args) {
        InputDto mobileBillData = new InputDto(25, 23);
        InputDto internetBillData = new InputDto(22, 23);
 
        Bill mobileBill = new MobileBillProduct();
        Bill internetBill = new IntegnetBillProduct();
    
        BillSystem.processBill(mobileBill, mobileBillData);
        BillSystem.processBill(internetBill, internetBillData);
        
        OutputDto mobileBillStatus = new OutputDto(mobileBill);
        OutputDto internetBillStatus = new OutputDto(internetBill);
        
        System.out.println(mobileBillStatus);
        System.out.println(internetBillStatus);
    }
}
