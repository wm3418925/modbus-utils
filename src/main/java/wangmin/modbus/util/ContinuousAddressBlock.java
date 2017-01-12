package wangmin.modbus.util;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by wm on 2016/12/30.
 * 将 不连续的数据块 拼接 为 连续的
 */
public class ContinuousAddressBlock {
    // 二分查找比key小的最大值
    private static <T> int indexedBinarySearchLower(List<? extends Comparable<? super T>> list, T key) {
        int low = 0;
        int high = list.size()-1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            Comparable<? super T> midVal = list.get(mid);
            int cmp = midVal.compareTo(key);

            if (cmp < 0) {
                low = mid + 1;

                if (low > high)
                    return mid;
            } else if (cmp > 0) {
                high = mid - 1;

                if (low > high)
                    return mid-1;
            } else {
                return mid-1; // key found
            }
        }
        return -(low + 1);  // key not found
    }

    public static class AddressBlock implements Comparable<AddressBlock> {
        public int home;
        public int end;

        public AddressBlock() {}
        public AddressBlock(int home, int end) {
            this.home = home;
            this.end = end;
        }

        @Override
        public String toString() {
            return "home="+home + ", end="+end;
        }

        @Override
        public int compareTo(AddressBlock o) {
            return this.home - o.home;
        }
    }

    private final List<AddressBlock> l = Lists.newArrayList();

    public void addBlock(int home, int end) {
        assert(0 <= home && home < end);

        int lower = indexedBinarySearchLower(l, new AddressBlock(home,end));
        if (-1 == lower)
            lower = 0;

        int size = l.size();
        for (int i=lower; i<size; ++i) {
            AddressBlock ab = l.get(i);
            boolean mergeRest = false;
            if (ab.home <= home && end <= ab.end) {
                return;
            } else if (home <= ab.home && ab.home <= end) {
                ab.home = home;

                if (ab.end >= end) {
                    return;
                } else {
                    ab.end = end;
                    mergeRest = true;
                }
            } else if (home <= ab.end && ab.end <= end) {
                ab.end = end;
                mergeRest = true;
            }

            // 与后面的合并
            if (mergeRest) {
                ++i;
                while (i < l.size()) {
                    AddressBlock ab2 = l.get(i);
                    if (end < ab2.home)
                        return;
                    if (end <= ab2.end) {
                        ab.end = ab2.end;
                        l.remove(i);
                        return;
                    } else {
                        l.remove(i);
                    }
                }
                return;
            }

            if (end < ab.home) {
                l.add(i, new AddressBlock(home, end));
                return;
            }
        }

        l.add(new AddressBlock(home, end));
    }

    /**
     * 根据数据的起始地址获取该数据所在数据块的索引
     * @return 返回正数代表找到, -1代表未找到
     * */
    public int getBlockIndexByStartAddress(int address) {
        int lower = indexedBinarySearchLower(l, new AddressBlock(address, address));
        if (-1 == lower)
            return -1;

        int size = l.size();
        for (int i=lower; i<size; ++i) {
            AddressBlock ab = l.get(i);
            if (ab.home <= address && address < ab.end)
                return i;
            if (address < ab.home)
                break;
        }
        return -1;
    }

    public List<AddressBlock> getBlocks() {return l;}
    public AddressBlock getBlock(int index) {return l.get(index);}
    public int getBlockCount() {return l.size();}



    public static void main(String[] argv) {
        ContinuousAddressBlock cab = new ContinuousAddressBlock();
        cab.addBlock(0, 4); for (AddressBlock ab : cab.getBlocks()) {System.out.print(ab.home + "-" + ab.end + " ");} System.out.println("");
        cab.addBlock(4, 8); for (AddressBlock ab : cab.getBlocks()) {System.out.print(ab.home + "-" + ab.end + " ");} System.out.println("");
        cab.addBlock(16, 20); for (AddressBlock ab : cab.getBlocks()) {System.out.print(ab.home + "-" + ab.end + " ");} System.out.println("");
        cab.addBlock(24, 28); for (AddressBlock ab : cab.getBlocks()) {System.out.print(ab.home + "-" + ab.end + " ");} System.out.println("");
        cab.addBlock(30, 34); for (AddressBlock ab : cab.getBlocks()) {System.out.print(ab.home + "-" + ab.end + " ");} System.out.println("");

        cab.addBlock(0, 4); for (AddressBlock ab : cab.getBlocks()) {System.out.print(ab.home + "-" + ab.end + " ");} System.out.println("");
        cab.addBlock(0, 8); for (AddressBlock ab : cab.getBlocks()) {System.out.print(ab.home + "-" + ab.end + " ");} System.out.println("");

        // 测试过 (0,7~35) (4,7~35) (8,9~35) (12,13~35) (16,17~35) (18,19~35)
        cab.addBlock(12, 15); for (AddressBlock ab : cab.getBlocks()) {System.out.print(ab.home + "-" + ab.end + " ");} System.out.println("");
    }
}
