package android.support.v4.app;

import java.util.Collections;

/**
 * Created by LiZheng on 2017//05/30 0025.
 * http://stackoverflow.com/questions/23504790/android-multiple-fragment-transaction-ordering
 */
public class FragmentTransactionBugFixHack {

    public static void reorderIndices(FragmentManager fragmentManager) {
        if (!(fragmentManager instanceof FragmentManagerImpl))
            return;
        try {
            //FragmentManagerImpl is not pulic ,however the class is final static
            FragmentManagerImpl fragmentManagerImpl = (FragmentManagerImpl) fragmentManager;
            if (fragmentManagerImpl.mAvailIndices != null && fragmentManagerImpl.mAvailIndices.size() > 1) {
//                System.out.println("排序前-->" + fragmentManagerImpl.mAvailIndices);
                Collections.sort(fragmentManagerImpl.mAvailIndices, Collections.reverseOrder());
//                System.out.println("排序后-->" + fragmentManagerImpl.mAvailIndices);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isStateSaved(FragmentManager fragmentManager) {
        if (!(fragmentManager instanceof FragmentManagerImpl))
            return false;
        try {
            FragmentManagerImpl fragmentManagerImpl = (FragmentManagerImpl) fragmentManager;
            // 从5年前一直到当前的Support-25.3.1,该字段没有变化过
            return fragmentManagerImpl.mStateSaved;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}