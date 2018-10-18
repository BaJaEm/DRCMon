package org.bajaem.drcmon.model;

import static org.junit.Assert.assertNotNull;

import org.bajaem.drcmon.DBGenerator;
import org.bajaem.drcmon.probe.PingProbe;
import org.bajaem.drcmon.probe.PingProbeConfig;
import org.bajaem.drcmon.probe.Response;
import org.bajaem.drcmon.respository.ProbeConfigRepository;
import org.bajaem.drcmon.respository.ProbeResponseRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;;

@WithMockUser(username = "admin", roles = { "USER", "ADMIN" })
public class ResponseTest extends DBGenerator
{
    @Autowired
    private ProbeResponseRepository repo;

    @Autowired
    private ProbeConfigRepository configRepo;

    @Test
    public void testTypeRepoExists()
    {
        assertNotNull(repo);
    }

    @Test
    public void testFenceposts()
    {
        final PingProbeConfig conf = newPingProbeConfig("foo.bar");
        configRepo.save(conf.getConfig());
        for (int sz = 2047; sz <= 2049; sz++)
        {
            final int t = sz;

            final PingProbe p = new PingProbe(conf)
            {
                @Override
                public Response probe()
                {
                    return new Response(false, stringOfLen(t), new Exception());
                }
            };
            p.run();
        }
    }

    private String stringOfLen(final int sz)
    {
        final char[] m = new char[sz];
        for (int x = 0; x < sz; x++)
        {
            m[x] = 'a';
        }

        return m.toString();
    }

}
