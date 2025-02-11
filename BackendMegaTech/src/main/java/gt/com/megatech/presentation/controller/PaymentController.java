package gt.com.megatech.presentation.controller;

import gt.com.megatech.persistence.entity.enums.MonthEnum;
import gt.com.megatech.presentation.dto.PaymentDTO;
import gt.com.megatech.presentation.dto.PaymentRequestDTO;
import gt.com.megatech.presentation.dto.StudentDTO;
import gt.com.megatech.presentation.dto.StudentLateDTO;
import gt.com.megatech.service.assembler.PaymentModelAssembler;
import gt.com.megatech.service.interfaces.IPaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(
        "/api/payment"
)
@RequiredArgsConstructor
@PreAuthorize(
        "denyAll()"
)
public class PaymentController {

    private final PaymentModelAssembler paymentModelAssembler;
    private final PagedResourcesAssembler<PaymentDTO> paymentDTOPagedResourcesAssembler;
    private final PagedResourcesAssembler<StudentDTO> studentDTOPagedResourcesAssembler;
    private final PagedResourcesAssembler<StudentLateDTO> studentLateDTOPagedResourcesAssembler;
    private final IPaymentService iPaymentService;

    @PreAuthorize(
            "hasAuthority('READ')"
    )
    @GetMapping
    public CollectionModel<EntityModel<PaymentDTO>> findAllPaymentsByMonthAndYear(
            @RequestParam MonthEnum monthEnum,
            @RequestParam Integer year
    ) {
        List<EntityModel<PaymentDTO>> payments = this.iPaymentService.findAllPaymentsByMonthAndYear(
                        monthEnum,
                        year
                )
                .stream()
                .map(paymentModelAssembler::toModel)
                .toList();
        return CollectionModel.of(
                payments,
                linkTo(methodOn(PaymentController.class).findAllPaymentsByMonthAndYear(
                        monthEnum,
                        year
                ))
                        .withSelfRel()
        );
    }

    @PreAuthorize(
            "hasAuthority('READ')"
    )
    @GetMapping(
            "/paged"
    )
    public ResponseEntity<PagedModel<EntityModel<PaymentDTO>>> findAllPaymentsByMonthAndYear(
            @RequestParam MonthEnum monthEnum,
            @RequestParam Integer year,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Pageable pageable
    ) {
        Pageable customPageable = PageRequest.of(
                page,
                size,
                pageable.getSort()
        );
        Page<PaymentDTO> paymentDTOPage = this.iPaymentService.findAllPaymentsByMonthAndYear(
                monthEnum,
                year,
                customPageable
        );
        PagedModel<EntityModel<PaymentDTO>> entityModelPagedModel = paymentDTOPagedResourcesAssembler
                .toModel(
                        paymentDTOPage
                );
        return ResponseEntity.ok(
                entityModelPagedModel
        );
    }

    @PreAuthorize(
            "hasAuthority('READ')"
    )
    @GetMapping(
            "/pending-payments"
    )
    public CollectionModel<EntityModel<StudentDTO>> findAllStudentsWithPendingPayments() {
        List<EntityModel<StudentDTO>> entityModelList = this.iPaymentService.findAllStudentsWithPendingPayments()
                .stream()
                .map(paymentModelAssembler::toModelForPendingPayment)
                .toList();
        return CollectionModel.of(
                entityModelList,
                linkTo(methodOn(PaymentController.class).findAllStudentsWithPendingPayments())
                        .withSelfRel()
        );
    }

    @PreAuthorize(
            "hasAuthority('READ')"
    )
    @GetMapping(
            "/paged-pending-payments"
    )
    public ResponseEntity<PagedModel<EntityModel<StudentDTO>>> findAllStudentsWithPendingPayments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Pageable pageable
    ) {
        Pageable customPageable = PageRequest.of(
                page,
                size,
                pageable.getSort()
        );
        Page<StudentDTO> studentDTOPage = this.iPaymentService.findAllStudentsWithPendingPayments(
                customPageable
        );
        PagedModel<EntityModel<StudentDTO>> entityModelPagedModel = studentDTOPagedResourcesAssembler
                .toModel(
                        studentDTOPage
                );
        return ResponseEntity.ok(
                entityModelPagedModel
        );
    }

    @PreAuthorize(
            "hasAuthority('READ')"
    )
    @GetMapping(
            "/late-payments"
    )
    public CollectionModel<EntityModel<StudentLateDTO>> findAllStudentsWithLatePayments() {
        List<EntityModel<StudentLateDTO>> entityModelList = this.iPaymentService.findAllStudentsWithLatePayments()
                .stream()
                .map(paymentModelAssembler::toModelForLatePayment)
                .toList();
        return CollectionModel.of(
                entityModelList,
                linkTo(methodOn(PaymentController.class).findAllStudentsWithLatePayments())
                        .withSelfRel()
        );
    }

    @PreAuthorize(
            "hasAuthority('READ')"
    )
    @GetMapping(
            "/paged-late-payments"
    )
    public ResponseEntity<PagedModel<EntityModel<StudentLateDTO>>> findAllStudentsWithLatePayments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Pageable pageable
    ) {
        Pageable customPageable = PageRequest.of(
                page,
                size,
                pageable.getSort()
        );
        Page<StudentLateDTO> studentLateDTOPage = this.iPaymentService.findAllStudentsWithLatePayments(
                customPageable
        );
        PagedModel<EntityModel<StudentLateDTO>> entityModelPagedModel = studentLateDTOPagedResourcesAssembler
                .toModel(
                        studentLateDTOPage
                );
        return ResponseEntity.ok(
                entityModelPagedModel
        );
    }

    @PreAuthorize(
            "hasAuthority('READ')"
    )
    @GetMapping(
            "/{id}"
    )
    public EntityModel<PaymentDTO> findByIdPayment(
            @PathVariable Long id
    ) {
        PaymentDTO payment = this.iPaymentService.findByIdPayment(
                id
        );
        return paymentModelAssembler
                .toModel(
                        payment
                );
    }

    @PreAuthorize(
            "hasAuthority('CREATE')"
    )
    @PostMapping
    public ResponseEntity<CollectionModel<PaymentDTO>> savePayments(
            @RequestBody @Valid PaymentRequestDTO paymentRequestDTO
    ) {
        List<PaymentDTO> savedPayments = this.iPaymentService.savePayments(
                paymentRequestDTO
        );
        CollectionModel<PaymentDTO> responseCollectionModel = CollectionModel.of(
                savedPayments,
                linkTo(methodOn(PaymentController.class).savePayments(paymentRequestDTO))
                        .withSelfRel()
        );
        return ResponseEntity
                .created(responseCollectionModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(responseCollectionModel);
    }

    @PreAuthorize("hasAuthority('UPDATE')")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<PaymentDTO>> updatePayment(
            @PathVariable Long id,
            @RequestBody @Valid PaymentRequestDTO paymentRequestDTO
    ) {
        PaymentDTO updatedPayment = this.iPaymentService.updatePayment(id, paymentRequestDTO);
        EntityModel<PaymentDTO> entityModel = paymentModelAssembler.toModel(updatedPayment);
        return ResponseEntity.ok(entityModel);
    }
}
