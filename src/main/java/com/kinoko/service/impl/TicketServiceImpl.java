package com.kinoko.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kinoko.entity.TicketRecord;
import com.kinoko.dao.TicketRecordMapper;
import com.kinoko.service.TicketRecordService;
import org.springframework.stereotype.Service;

@Service
public class TicketServiceImpl extends ServiceImpl<TicketRecordMapper, TicketRecord> implements TicketRecordService {
}
