package com.eurlanda.edu.tp.webdomain.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by test on 2018/4/23.
 */
public class RepClouderaData {

    private List<Map> cpuData = new ArrayList<>();
    private List<Map> memoryData = new ArrayList<>();
    private List<Map> networkData = new ArrayList<>();
    private List<Map> disksData = new ArrayList<>();


    public List<Map> getCpuData() {
        return cpuData;
    }

    public void setCpuData(List<Map> cpuData) {
        this.cpuData = cpuData;
    }

    public List<Map> getMemoryData() {
        return memoryData;
    }

    public void setMemoryData(List<Map> memoryData) {
        this.memoryData = memoryData;
    }

    public List<Map> getNetworkData() {
        return networkData;
    }

    public void setNetworkData(List<Map> networkData) {
        this.networkData = networkData;
    }

    public List<Map> getDisksData() {
        return disksData;
    }

    public void setDisksData(List<Map> disksData) {
        this.disksData = disksData;
    }
}
