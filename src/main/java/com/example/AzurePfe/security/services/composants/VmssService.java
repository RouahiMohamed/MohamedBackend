package com.example.AzurePfe.security.services.composants;

import com.example.AzurePfe.models.composant.ResourceGroup;
import com.example.AzurePfe.models.composant.VirtualMachine;
import com.example.AzurePfe.models.composant.Vmss;
import com.example.AzurePfe.repository.composants.VirtualMachineRepository;
import com.example.AzurePfe.repository.composants.VmssRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class VmssService {

    @Autowired
    private VirtualMachineService virtualMachineService;

    @Autowired
    private VmssRepository vmssRepository;

    public Vmss createVmss(Vmss vmss) {
        VirtualMachine vm = vmss.getVirtualMachine();
        if (vm != null) {
            virtualMachineService.saveVirtualMachine(vm);
        }
        return vmssRepository.save(vmss);
    }

    public Vmss getVmssById(String id) {
        return vmssRepository.findById(id).orElse(null);
    }

    public List<Vmss> getAllVmss() {
        return vmssRepository.findAll();
    }
    public void deleteVmssById(String id) {
        vmssRepository.deleteById(id);
    }

}
