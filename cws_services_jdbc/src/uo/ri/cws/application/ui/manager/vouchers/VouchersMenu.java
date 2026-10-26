package uo.ri.cws.application.ui.manager.vouchers;

import uo.ri.cws.application.ui.manager.vouchers.action.FindVouchersByClientNifAction;
import uo.ri.cws.application.ui.manager.vouchers.action.GenerateVouchersAction;
import uo.ri.cws.application.ui.manager.vouchers.action.GetVoucherSummaryAction;
import uo.ri.util.menu.BaseMenu;

public class VouchersMenu extends BaseMenu {

    public VouchersMenu() {
        menuOptions = new Object[][] { { "Manager > Voucher management", null },
            { "Generate vouchers", GenerateVouchersAction.class },
            { "List vouchers by client nif", FindVouchersByClientNifAction.class },
            { "List vouchers summary", GetVoucherSummaryAction.class } };
    }

    public static void main(String[] args) {
        new VouchersMenu().execute();
    }

}